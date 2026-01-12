<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>table</title>
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
              <h3 class="page-title"> Table </h3>

            </div>
            <div class="row">
              <div class="col-lg-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Hoverable Table</h4>
                    </p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>User</th>
                            <th>Product</th>
                            <th>Sale</th>
                            <th>Status</th>
                            <th>Action</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>Jacob</td>
                            <td>Photoshop</td>
                            <td class="text-danger"> 28.76% <i class="mdi mdi-arrow-down"></i></td>
                            <td><label class="badge badge-danger">Pending</label></td>
                            <td>
                              <a href="see">
                                <i class="mdi mdi-eye btn btn-info"></i>
                              </a>
                              <a href="modify">
                                <i class="mdi mdi-pen btn btn-primary"></i>
                              </a>
                              <a href="delete">
                                <i class="mdi mdi-delete btn btn-danger"></i>
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>Messsy</td>
                            <td>Flash</td>
                            <td class="text-danger"> 21.06% <i class="mdi mdi-arrow-down"></i></td>
                            <td><label class="badge badge-warning">In progress</label></td>
                            <td>
                              <a href="see">
                                <i class="mdi mdi-eye btn btn-info"></i>
                              </a>
                              <a href="modify">
                                <i class="mdi mdi-pen btn btn-primary"></i>
                              </a>
                              <a href="delete">
                                <i class="mdi mdi-delete btn btn-danger"></i>
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>John</td>
                            <td>Premier</td>
                            <td class="text-danger"> 35.00% <i class="mdi mdi-arrow-down"></i></td>
                            <td><label class="badge badge-info">Fixed</label></td>
                            <td>
                              <a href="see">
                                <i class="mdi mdi-eye btn btn-info"></i>
                              </a>
                              <a href="modify">
                                <i class="mdi mdi-pen btn btn-primary"></i>
                              </a>
                              <a href="delete">
                                <i class="mdi mdi-delete btn btn-danger"></i>
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>Peter</td>
                            <td>After effects</td>
                            <td class="text-success"> 82.00% <i class="mdi mdi-arrow-up"></i></td>
                            <td><label class="badge badge-success">Completed</label></td>
                            <td>
                              <a href="see">
                                <i class="mdi mdi-eye btn btn-info"></i>
                              </a>
                              <a href="modify">
                                <i class="mdi mdi-pen btn btn-primary"></i>
                              </a>
                              <a href="delete">
                                <i class="mdi mdi-delete btn btn-danger"></i>
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>Dave</td>
                            <td>53275535</td>
                            <td class="text-success"> 98.05% <i class="mdi mdi-arrow-up"></i></td>
                            <td><label class="badge badge-warning">In progress</label></td>
                            <td>
                              <a href="see">
                                <i class="mdi mdi-eye btn btn-info"></i>
                              </a>
                              <a href="modify">
                                <i class="mdi mdi-pen btn btn-primary"></i>
                              </a>
                              <a href="delete">
                                <i class="mdi mdi-delete btn btn-danger"></i>
                              </a>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
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