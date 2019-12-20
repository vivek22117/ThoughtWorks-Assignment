provider "aws" {
  region  = var.default_region                         // Interpolation Syntax
  profile = var.profile

  version = "2.17.0"                                    // AWS plugin version
}


######################################################
# Terraform configuration block is used to define backend
# Interpolation sytanx is not allowed in Backend
######################################################
terraform {
  required_version = ">= 0.12"                             // Terraform version

  backend "s3" {
    profile        = "doubledigit"
    region         = "us-east-1"
    encrypt        = "true"
  }
}

