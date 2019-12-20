output "equipment_iot_app_group_name" {
  value = aws_codedeploy_deployment_group.equipment_iot_app_group_name.deployment_group_name
}

output "equipment_iot_name" {
  value = aws_codedeploy_app.equipment_iot_name.name
}
