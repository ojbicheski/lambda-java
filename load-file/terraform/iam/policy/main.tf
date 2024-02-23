resource "aws_iam_policy" "policy" {
  name        = "${var.name}-policy"
  description = var.desc

  policy      = <<EOT
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:PutLogEvents",
        "logs:CreateLogGroup",
        "logs:CreateLogStream"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject"
      ],
      "Resource": "arn:aws:s3:::*/*"
    }
  ]
}
EOT
}

output "name" {
  value = aws_iam_policy.policy.name
}
output "arn" {
  value = aws_iam_policy.policy.arn
}