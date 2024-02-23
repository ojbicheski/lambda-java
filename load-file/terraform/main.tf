provider "aws" {
    region = var.region
}

module "s3" {
    source      = "./s3"
    #bucket name should be unique
    bucket_name = "s3-upload-file"
}
output "a_s3_arn" {
    value = module.s3.arn
}

module "policy" {
    source     = "./iam/policy"
    name       = "lambda-s3-trigger"
    desc       = "S3 upload file policy"
    s3_arn     = module.s3.arn
    depends_on = [ module.s3 ]
}
output "b_policy_arn" {
    value = module.policy.arn
}

module "role" {
    source      = "./iam/role"
    policy_arn  = module.policy.arn
    role_name   = "lambda-s3-trigger"
    depends_on  = [ module.s3, module.policy ]
}
output "c_role_arn" {
    value = module.role.arn
}

module "lambda" {
    source     = "./lambda"
    bucket_id  = module.s3.id
    bucket_arn = module.s3.arn
    role_arn   = module.role.arn
    depends_on = [ module.s3, module.policy, module.role ]
}
output "d_lambda_arn" {
    value = module.lambda.arn
}
output "e_lambda_s3_notify" {
    value = module.lambda.s3_notify_id
}
