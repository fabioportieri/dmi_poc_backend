#!/bin/sh

mc alias set local http://minio:9000 rbt6CNmVEl1UiaAS6viB Q9jbat71jYHv1ySgjay7Mfyp01tV0c9lYYIXCa6d

# Wait until Minio is ready
until mc ls local; do
  echo "Waiting for Minio to be ready..."
  sleep 1
done

# Check if the bucket "test-bucket" exists
if mc ls local/test-bucket 2>/dev/null; then
  echo "Bucket test-bucket already exists. Exiting..."
  exit 0
fi

# Execute your mc commands

#mc admin info local

mc mb local/test-bucket
echo "created bucket test-bucket"
mc admin user add local pDLNypgWK4n4IidH 6pvgqJI9Bhxn0bEYp5NEk8xS8wDHW4ox
echo "created credentials for bucket and app dmi_poc"

# Define a policy in a JSON file
echo '{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:ListBucket", "s3:GetBucketLocation"],
      "Resource": ["arn:aws:s3:::test-bucket"]
    },
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject", "s3:PutObject", "s3:ListMultipartUploadParts", "s3:AbortMultipartUpload"],
      "Resource": ["arn:aws:s3:::test-bucket/*"]
    }
  ]
}' > /tmp/minio-policy.json

# Set the policy for the user

mc admin policy create local minio-policy /tmp/minio-policy.json

# Attach the policy to the user
mc admin policy attach local minio-policy --user pDLNypgWK4n4IidH






