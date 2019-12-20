data "terraform_remote_state" "vpc" {
  backend = "s3"

  config = {
    profile = "doubledigit"
    bucket  = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key     = "state/${var.environment}/vpc/terraform.tfstate"
    region  = var.default_region
  }
}

data "terraform_remote_state" "backend" {
  backend = "s3"

  config = {
    profile = "doubledigit"
    bucket  = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key     = "state/${var.environment}/aws/terraform.tfstate"
    region  = var.default_region
  }
}

data "terraform_remote_state" "code_deploy_backend" {
  backend = "s3"

  config = {
    profile = "doubledigit"
    bucket  = "${var.s3_bucket_prefix}-${var.environment}-${var.default_region}"
    key     = "state/${var.environment}/equipment-iot/code-deploy/terraform.tfstate"
    region  = var.default_region
  }
}

data "template_file" "ec2_user_data" {
  template = file("${path.module}/script/user-data.sh")

  vars = {
    environment = var.environment
    deploy_bucket = data.terraform_remote_state.backend.outputs.deploy_bucket_name
    app_key = var.ec2-webapp-bucket-key
    app_group_name = data.terraform_remote_state.code_deploy_backend.outputs.equipment_iot_app_group_name
    app_name = data.terraform_remote_state.code_deploy_backend.outputs.equipment_iot_name
  }
}