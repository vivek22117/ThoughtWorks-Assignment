# adding the zip/jar to the defined bucket
resource "aws_s3_bucket_object" "ec2-app-package" {
  bucket                 = data.terraform_remote_state.backend.outputs.deploy_bucket_name
  key                    = var.ec2-webapp-bucket-key
  source                 = "${path.module}/../../spring-app/target/ThoughtWorks-Assignment-1.0-webapp.zip"
  etag   = filemd5("${path.module}/../../spring-app/target/ThoughtWorks-Assignment-1.0-webapp.zip")
}

resource "aws_launch_template" "equipment_iot_launch_template" {
  name_prefix            = "${var.resource_name_prefix}${var.environment}"
  image_id               = var.ami_id
  instance_type          = var.instance_type
  key_name               = var.key_name

  user_data = base64encode(data.template_file.ec2_user_data.rendered)

  instance_initiated_shutdown_behavior = "terminate"

  iam_instance_profile {
    arn = aws_iam_instance_profile.equipment_iot_profile.arn
  }

  network_interfaces {
    device_index = 0
    associate_public_ip_address = true
    security_groups = [aws_security_group.instance_sg.id]
    delete_on_termination = true
  }

  placement {
    tenancy = "default"
  }

  block_device_mappings {
    device_name = "/dev/xvda"

    ebs {
      volume_size           = var.volume_size
      volume_type           = "gp2"
      delete_on_termination = true
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_lb" "equipment_iot_lb" {
  name               = var.equipment_iot_lb
  load_balancer_type = "application"
  subnets            = data.terraform_remote_state.vpc.outputs.public_subnets
  internal           = "false"
  security_groups = [aws_security_group.lb_sg.id]

  tags = {
    Name = "${var.equipment_iot_lb}-${var.environment}"
  }
}

resource "aws_lb_listener" "equipment_iot_lb_listener" {
  load_balancer_arn = aws_lb.equipment_iot_lb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.equipment_iot_lb_target_group.arn
  }
}

resource "aws_alb_listener_rule" "listener_rule" {
  depends_on   = ["aws_lb_target_group.equipment_iot_lb_target_group"]

  listener_arn = aws_lb_listener.equipment_iot_lb_listener.arn
  priority     = "100"

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.equipment_iot_lb_target_group.arn
  }
  condition {
    field  = "path-pattern"
    values = ["/"]
  }
}

resource "aws_lb_target_group" "equipment_iot_lb_target_group" {
  name     = "${var.resource_name_prefix}${var.environment}-tg"
  port     = var.target_group_port
  protocol = "HTTP"
  vpc_id   = data.terraform_remote_state.vpc.outputs.vpc_id
  target_type = "instance"

  tags = {
    name = "${var.resource_name_prefix}tg"
  }

  health_check {
    enabled = true
    protocol = "HTTP"
    healthy_threshold   = 3
    unhealthy_threshold = 10
    timeout             = 5
    interval            = 10
    path                = var.target_group_path
    port                = var.target_group_port
  }
}

resource "aws_autoscaling_group" "equipment_iot_asg" {
  depends_on = ["aws_s3_bucket_object.ec2-app-package"]

  name_prefix         = "equipment-iot-asg-${var.environment}"
  vpc_zone_identifier = data.terraform_remote_state.vpc.outputs.public_subnets

  launch_template {
    id      = aws_launch_template.equipment_iot_launch_template.id
    version = aws_launch_template.equipment_iot_launch_template.latest_version
  }
  target_group_arns = [aws_lb_target_group.equipment_iot_lb_target_group.arn]

  termination_policies      = var.termination_policies
  max_size                  = "${var.environment == "PROD" ? var.equipment_iot_asg_max_size : 1}" 
  min_size                  = "${var.environment == "PROD" ? var.equipment_iot_asg_min_size : 1}" 
  desired_capacity          = "${var.environment == "PROD" ? var.equipment_iot_asg_desired_capacity : 1}" 
  health_check_grace_period = var.equipment_iot_asg_health_check_grace_period
  health_check_type         = var.health_check_type
  wait_for_elb_capacity     = var.equipment_iot_asg_wait_for_elb_capacity
  wait_for_capacity_timeout = var.wait_for_capacity_timeout

  default_cooldown = var.default_cooldown

  tag {
    key                 = "Name"
    value               = var.app_instance_name
    propagate_at_launch = true
  }

  tag {
    key                 = "owner"
    value               = local.common_tags.owner
    propagate_at_launch = true
  }

  tag {
    key                 = "team"
    value               = local.common_tags.team
    propagate_at_launch = true
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_autoscaling_attachment" "attach_equipment_iot_asg_tg" {
  autoscaling_group_name = aws_autoscaling_group.equipment_iot_asg.id
  alb_target_group_arn   = aws_lb_target_group.equipment_iot_lb_target_group.arn
}

resource "aws_cloudwatch_metric_alarm" "cpu-alarm-scaling-up" {
alarm_name = "equipment-iot-app-cpu-alarm"
alarm_description = "equipment-iot-app-cpu-alarm"
comparison_operator = "GreaterThanOrEqualToThreshold"
evaluation_periods = "1"
metric_name = "CPUUtilization"
namespace = "AWS/EC2"
period = "300"
statistic = "Average"
threshold = "80"
dimensions = {
  "AutoScalingGroupName" = aws_autoscaling_group.equipment_iot_asg.name
}
actions_enabled = true
alarm_actions = [aws_autoscaling_policy.instance_scaling_up_policy.arn]
}

resource "aws_autoscaling_policy" "instance_scaling_up_policy" {
  autoscaling_group_name = aws_autoscaling_group.equipment_iot_asg.name
  name                   = "equipment_iot_asg_scaling_up"
  scaling_adjustment     = 1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 600
}

resource "aws_cloudwatch_metric_alarm" "cpu-alarm-scaling-down" {
alarm_name = "equipment-iot-app-cpu-alarm-down"
alarm_description = "equipment-iot-app-cpu-alarm-down"
comparison_operator = "GreaterThanOrEqualToThreshold"
evaluation_periods = "1"
metric_name = "CPUUtilization"
namespace = "AWS/EC2"
period = "300"
statistic = "Average"
threshold = "20"
dimensions = {
  "AutoScalingGroupName" = aws_autoscaling_group.equipment_iot_asg.name
}
actions_enabled = true
alarm_actions = [aws_autoscaling_policy.instance_scaling_down_policy.arn]
}

resource "aws_autoscaling_policy" "instance_scaling_down_policy" {
  autoscaling_group_name = aws_autoscaling_group.equipment_iot_asg.name
  name                   = "equipment_iot_asg_scaling_down"
  scaling_adjustment     = -1
  adjustment_type        = "ChangeInCapacity"
  cooldown               = 600
}

