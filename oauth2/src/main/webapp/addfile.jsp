<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Image Uploading Form</title>
    </head>

    <body>
        <h3>File Upload:</h3>
        Select a file to upload: <br />
        <form action="uploadservlet" method="post" enctype="multipart/form-data">
            <input type="file" name="file" accept=".jpg .jpeg"/>
            </br>
            </br>
            <input type="submit" value="Upload ot GoogleDrive"/>
        </form>
    </body>

</html>
