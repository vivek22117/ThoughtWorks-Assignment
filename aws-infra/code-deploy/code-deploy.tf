resource "aws_codedeploy_app" "iot_equipment_codedeploy_app" {
  name = "ThoughtWorks_Assignment"
  compute_platform = "Server"
}

resource "aws_codedeploy_deployment_group" "iot_equipment_codedeploy_group" {
  app_name              = aws_codedeploy_app.iot_equipment_codedeploy_app.name
  deployment_group_name = "ThoughtWorksAssignment"
  service_role_arn      = aws_iam_role.rsvp_codedeploy_role.arn

  deployment_config_name = "CodeDeployDefault.OneAtATime"                      # AWS defined deployment config

  ec2_tag_filter {
    key   = "Name"
    value = var.app_instance_name
    type  = "KEY_AND_VALUE"
  }

  # trigger a rollback on deployment failure event
  auto_rollback_configuration {
    enabled = true
    events = [
      "DEPLOYMENT_FAILURE"
    ]
  }
}