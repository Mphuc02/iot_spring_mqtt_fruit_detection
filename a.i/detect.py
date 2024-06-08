import os
from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
from ultralytics import YOLO
import cv2
from PIL import Image
import numpy as np
import io
import base64

from flask import Flask, jsonify, send_file, render_template_string
from ultralytics import YOLO
from PIL import Image
import io
import numpy as np

app = Flask(__name__)
# Load the YOLO model
model = YOLO('best.pt')

CORS(app, resources={r"/upload": {"origins": "http://localhost:8081"}})

@app.route('/upload', methods=['POST'])
def upload_image():
    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400

    file = request.files['image']
    if file.filename == '':
        return jsonify({"error": "No image file provided"}), 400

    # Read the image from the uploaded file
    img = Image.open(file.stream)
    img = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

    # Run inference
    results = model([img], iou=0.5)

    # Process results
    for result in results:
        result_img = result.plot()  # Plotting the results

    # Convert to PIL Image and save to BytesIO
    result_pil_img = Image.fromarray(cv2.cvtColor(result_img, cv2.COLOR_BGR2RGB))
    img_io = io.BytesIO()
    result_pil_img.save(img_io, 'JPEG', quality=70)
    img_io.seek(0)

    # Encode image to base64
    img_base64 = base64.b64encode(img_io.getvalue()).decode('utf-8')

    return jsonify({"image": img_base64})

if __name__ == '__main__':
    app.run(debug=True)
