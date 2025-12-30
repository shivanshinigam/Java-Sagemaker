import os
import subprocess
import boto3

print("Runner started")

# Get S3 path from environment variable
s3_path = os.environ.get("USER_SCRIPT_S3_PATH")

if not s3_path:
    raise Exception("USER_SCRIPT_S3_PATH not set")

print("User script S3 path:", s3_path)

# Parse bucket and key
bucket = s3_path.replace("s3://", "").split("/")[0]
key = "/".join(s3_path.replace("s3://", "").split("/")[1:])

local_file = "/opt/ml/code/user_code.py"

# Download file
s3 = boto3.client("s3")
s3.download_file(bucket, key, local_file)

print("Downloaded user code, executing...")

subprocess.run(["python", local_file], check=True)

print("Runner finished")
