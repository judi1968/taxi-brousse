<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.TypeClient" %>
<%
    List<TypeClient> type_clients = (List<TypeClient>) request.getAttribute("type_clients");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des type_clients</title>
    <%@ include file="../../includes/css.jsp" %>
  </head>
  <body>
    <div class="container-scroller">
      <%@ include file="../../includes/navbar.jsp" %>
      <div class="container-fluid page-body-wrapper">
        <%@ include file="../../includes/header.jsp" %>
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="page-header">
              <h3 class="page-title">Liste des type_clients</h3>
            </div>
            
            <% if (error != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <%= error %>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <% } %>
            
            <div class="row">
              <div class="col-lg-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title">Liste des type_clients</h4>
                    <p class="card-description">Tous les type_clients disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>Nom</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (type_clients != null && !type_clients.isEmpty()) {
                               for (TypeClient type_client : type_clients) { 
%>
                          <tr>
                            <td><%= type_client.getId() %></td>
                            <td><%= type_client.getNom() %></td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="2" class="text-center">Aucun type_client trouvé</td>
                          </tr>
                          <% } %>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <%@ include file="../../includes/footer.jsp" %>
        </div>
      </div>
    </div>
    <%@ include file="../../includes/js.jsp" %>
  </body>
</html>
