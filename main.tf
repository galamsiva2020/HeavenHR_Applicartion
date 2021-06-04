provider "aws" {
  region     = "ap-south-1"
}

resource "aws_s3_bucket" "bucket1" {
  bucket = "my-bucket-2021-29-05"
  acl    = "private"

  tags = {
    Environment = "Dev"
  }
}

resource "aws_s3_bucket_object" "object" {
  bucket = "my-bucket-2021-29-05"
  key    = "Terraform impl"
  source = "C:\\Users\\siva\\Desktop\\AWS\\Terraform impl.txt"
}

resource "null_resource" "multiple" {
  provisioner "local-exec" {
    command = "aws s3 sync E:\\SivaPhotos  s3://my-bucket-2021-29-05"
  }
}
