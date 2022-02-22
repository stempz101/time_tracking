<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Title</title>
</head>
<body>

<div class="d-flex justify-content-center align-items-center h-100" id="main">
    <div class="inline-block align-middle text-center">
        <h4 class="font-weight-normal" id="desc">Sorry, but you have been blocked.</h4>
        <br>
        <a href="<%=request.getContextPath() + "/logout"%>" class="btn btn-secondary">Logout</a>
    </div>
</div>
<a href="<%=request.getContextPath() + "/logout"%>" class="btn btn-secondary"><h4 class="mb-0">Logout</h4></a>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
