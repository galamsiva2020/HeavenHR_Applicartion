provider "aws" {
  region     = "var.region"
}

resource "aws_instance" "server" {
count =2
  ami           = var.ami
  instance_type = var.int_type
  subnet_id =var.subnet
  security_groups =var.sec_grp
  key_name =sample

  tags = {
    Name = "terraform-${count.index+1}"