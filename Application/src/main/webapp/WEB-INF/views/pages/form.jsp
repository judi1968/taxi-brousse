<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Form</title>
    <!-- plugins:css -->
    <%@ include file="../includes/css.jsp" %>
  </head>
  <body>
    <div class="container-scroller">
     
      <!-- partial:partials/_sidebar.html -->
      
      <%@ include file="../includes/navbar.jsp" %>
      <!-- partial -->
      <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_navbar.html -->
        <%@ include file="../includes/header.jsp" %>
        <!-- partial -->
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title"> Formulaire </h3>

            </div>
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Horizontal Form</h4>
                    <p class="card-description"> Horizontal form layout </p>
                    <form class="forms-sample">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="exampleInputUsername2" class="col-sm-3 col-form-label">Email</label>
                            <div class="col-sm-9">
                              <input type="text" class="form-control" id="exampleInputUsername2" placeholder="Username">
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="exampleInputEmail2" class="col-sm-3 col-form-label">Email</label>
                            <div class="col-sm-9">
                              <input type="email" class="form-control" id="exampleInputEmail2" placeholder="Email">
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="exampleInputMobile" class="col-sm-3 col-form-label">Mobile</label>
                            <div class="col-sm-9">
                              <input type="text" class="form-control" id="exampleInputMobile" placeholder="Mobile number">
                            </div>
                          </div>
                          
                        </div>
                        <div class="col-md-6">
                          <div class="form-group row">
                            <label for="exampleInputUsername2" class="col-sm-3 col-form-label">Select</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="exampleFormControlSelect1">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option>4</option>
                                <option>5</option>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="exampleInputPassword2" class="col-sm-3 col-form-label">Password</label>
                            <div class="col-sm-9">
                              <input type="password" class="form-control" id="exampleInputPassword2" placeholder="Password">
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="exampleInputConfirmPassword2" class="col-sm-3 col-form-label">Re Password</label>
                            <div class="col-sm-9">
                              <input type="password" class="form-control" id="exampleInputConfirmPassword2" placeholder="Password">
                            </div>
                          </div>
                          <div class="form-check form-check-flat form-check-primary">
                            <label class="form-check-label">
                              <input type="checkbox" class="form-check-input"> Remember me </label>
                          </div>
                        </div>
                      </div>
                      <button type="submit" class="btn btn-primary me-2">Submit</button>
                      <button class="btn btn-dark">Cancel</button>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- content-wrapper ends -->
          <!-- partial:../../partials/_footer.html -->
          
          <%@ include file="../includes/footer.jsp" %>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <%@ include file="../includes/js.jsp" %>
  </body>
</html>