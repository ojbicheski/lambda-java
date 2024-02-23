variable "file" {
  type = string
  default = "../target/load-file-0.0.1-SNAPSHOT.jar"
}

variable "role_arn" {
  type = string
}

variable "bucket_id" {
  type = string
}

variable "bucket_arn" {
  type = string
}

variable "runtime" {
  type = string
  default = "java17"
}