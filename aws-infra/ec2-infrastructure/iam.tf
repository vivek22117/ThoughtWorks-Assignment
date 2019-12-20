resource "aws_iam_role" "equipment_iot_app_role" {
  name = "EquipmentIOTEC2Role"
  path = "/"

  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": "sts:AssumeRole",
            "Principal": {
               "Service": "ec2.amazonaws.com"
            },
            "Effect": "Allow",
            "Sid": ""
        }
    ]
}
EOF
}

resource "aws_iam_policy" "equipment_iot_policy" {
  name = "EquipmentIOTEC2Policy"
  description = "Policy to access AWS Resources"
  path = "/"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
	  "Action": [
	    "s3:ListBucket"
      ],
	  "Effect": "Allow",
	  "Resource": [
        "arn:aws:s3:::teamconcept-deploy-*/*",
        "arn:aws:s3:::teamconcept-deploy-*"
			]
	},
	{
	  "Action": [
	    "s3:DeleteObject",
		"s3:Get*",
		"s3:List*",
		"s3:Put*"
	  ],
	  "Effect": "Allow",
	  "Resource": [
        "arn:aws:s3:::teamconcept-deploy-*/*",
        "arn:aws:s3:::teamconcept-deploy-*"
	  ]
	}
  ]
}
EOF

}

resource "aws_iam_role_policy_attachment" "ec2_policy_role_attach" {
  policy_arn = aws_iam_policy.equipment_iot_policy.arn
  role       = aws_iam_role.equipment_iot_app_role.name
}

resource "aws_iam_instance_profile" "equipment_iot_profile" {
  name = "EquipmentIOTTierProfile"
  role = aws_iam_role.equipment_iot_app_role.name
}

