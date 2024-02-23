data "aws_iam_policy_document" "policy_document" {
  statement {
    effect  = "Allow"
    actions = [
      "sts:AssumeRole",
      "sts:TagSession",
      "sts:SetSourceIdentity"
    ]
    principals {
      type        = "Service"
      identifiers = [ "lambda.amazonaws.com" ]
    }
  }
}

resource "aws_iam_role" "assume_role" {
  name                = "${var.role_name}-role"
  assume_role_policy  = data.aws_iam_policy_document.policy_document.json
  tags                = {}
}

resource "aws_iam_role_policy_attachment" "aws_iam_role_policy_attachment" {
  role       = aws_iam_role.assume_role.name
  policy_arn = var.policy_arn
  depends_on = [ aws_iam_role.assume_role ]
}

output "arn" {
  value = aws_iam_role.assume_role.arn
}