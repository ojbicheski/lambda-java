resource "aws_s3_bucket" "s3-to-trigger-file-load" {
  bucket = var.bucket_name
}

output "id" {
  value = aws_s3_bucket.s3-to-trigger-file-load.id
}

output "arn" {
  value = aws_s3_bucket.s3-to-trigger-file-load.arn
}

output "bucket" {
  value = aws_s3_bucket.s3-to-trigger-file-load.bucket
}