resource "aws_lambda_function" "lambda-s3-trigger-load-file" {
  function_name    = "lambda-s3-trigger-load-file"
  role             = var.role_arn
  handler          = "ca.bicheski.aws.lambda.loadfile.S3FileHandler::handleRequest"
  runtime          = var.runtime
  timeout          = 30
  filename         = var.file
#  source_code_hash = filebase64sha256(var.file)
}

resource "aws_lambda_permission" "allow_bucket" {
   statement_id = "AllowExecutionFromS3Bucket"
   action = "lambda:InvokeFunction"
   function_name = aws_lambda_function.lambda-s3-trigger-load-file.function_name
   principal = "s3.amazonaws.com"
   source_arn = var.bucket_arn

  depends_on = [aws_lambda_function.lambda-s3-trigger-load-file]
}

resource "aws_s3_bucket_notification" "lambda-trigger" {
  bucket = var.bucket_id
  lambda_function {
    lambda_function_arn = aws_lambda_function.lambda-s3-trigger-load-file.arn
    events              = [ "s3:ObjectCreated:*" ]
    #    filter_prefix       = "folder/"
    #    filter_suffix       = ".csv"
  }
  depends_on = [ aws_lambda_permission.allow_bucket ]
}
output "s3_notify_id" {
  value = aws_s3_bucket_notification.lambda-trigger.id
}

output "arn" {
  value = aws_lambda_function.lambda-s3-trigger-load-file.arn
}