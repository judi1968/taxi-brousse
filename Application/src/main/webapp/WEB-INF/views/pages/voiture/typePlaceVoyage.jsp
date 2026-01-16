<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Modification type des places par voyage </title>
    <!-- plugins:css -->
    <%@ include file="../../includes/css.jsp" %>
  </head>
  <body>
    <div class="container-scroller">
     
      <!-- partial:partials/_sidebar.html -->
      
      <%@ include file="../../includes/navbar.jsp" %>
      <!-- partial -->
      <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_navbar.html -->
        <%@ include file="../../includes/header.jsp" %>
        <!-- partial -->
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title">Modification type des places par voyage</h3>

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
                          <div class="col-md-6">
                          <div class="form-group row">
                            <label for="exampleInputUsername2" class="col-sm-3 col-form-label">Type place</label>
                            <div class="col-sm-9">
                              <select class="form-control" id="exampleFormControlSelect1">
                                <option>Standar</option>
                                <option>Premium</option>
                              </select>
                            </div>
                          </div>
                          <div class="form-group row">
                            <label for="exampleInputUsername2" class="col-sm-5 col-form-label">Nom place</label>
                            <div class="col-sm-5">
                              <select class="form-control" id="exampleFormControlSelect1">
                                <option>P1</option>
                                <option>P2</option>
                                <option>P3</option>
                                <option>P4</option>
                                <option>P5</option>
                              </select>
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
          
          <%@ include file="../../includes/footer.jsp" %>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <%@ include file="../../includes/js.jsp" %>
  </body>
</html>