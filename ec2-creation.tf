provider "aws" {
  region     = "ap-south-1"
}

resource "aws_instance" "server" {
  ami           = "ami-0b3acf3edf2397475"
  instance_type = "t2.micro"
  subnet_id ="subnet-079253911ae9b068a"
  security_groups =["sg-09a51a57b5d5c30aa"]
  key_name =aws_key_pair.key.id

  tags = {
    Name = "terraform-server"
  }
}
resource "aws_key_pair" "key" {
 key_name   = "sample"
 public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDVHHy1oYuF9gBODQcK/3ITNm4Jjb/ZC3/o5ROjPAWaC38eWrh+y/6nGVF9UYNILtf5URe41FBDNAZ/bRM8iDR/q9wdNwUU6+Sh2sHcMv3FV8ny4aLk1fW7YVkeY187zX/a4y9+B9yYs8NqrpgbWCRMq3ws7tMD+kXj7duLQlMLoXLR2CPZ3BcnLDWk+jU9eSpIVaoo6qJQzi155xNRQxzHNZa8vKlFzQ1vCKeOaBf3gcExbn6dsEgsmPw2HPKR8vhKCWIpoVBdxbX9pXqchwlFPpxeac1f6BZjvNXepls+TJvcyJPwaOfMyecRrViFmh3nZf33s5yn5ETRpnqYTYAMWMCsk9AUbg1AEXGg3PcQLL2S16sfYpr8KQzSMW3QuCnPwGZ2CymFw14GHUExOyO413GapP6V0a2AVJ4g7jX2AiatdH76+DWYY/IuthZqd2VydzOPIIAGrSV5rr4Ty+/3SK3rzc4TErr28/JmscCx1xPZDnjZtaWS+4k/Rf0FQH8= siva@ADMINRG-BRG87EF"
  }