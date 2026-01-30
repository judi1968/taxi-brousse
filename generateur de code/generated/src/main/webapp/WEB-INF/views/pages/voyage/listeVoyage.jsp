<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.Voyage" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<Voyage> voyages = (List<Voyage>) request.getAttribute("voyages");
    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des voyages</title>
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
              <h3 class="page-title">Liste des voyages</h3>
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
                    <h4 class="card-title">Liste des voyages</h4>
                    <p class="card-description">Tous les voyages disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>Nom</th>
                            <th>Date</th>
                            <th>IdGareDepart</th>
                            <th>IdGareArrive</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (voyages != null && !voyages.isEmpty()) {
                               for (Voyage voyage : voyages) { 
%>
                          <tr>
                            <td><%= voyage.getId() %></td>
                            <td><%= voyage.getNom() %></td>
                            <td>
                              <% if (voyage.getDateObject() != null) { %>
                                <%= sdf.format(voyage.getDateObject()) %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (voyage.getGare_depart() != null) { %>
                                <%= voyage.getGare_depart().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td>
                              <% if (voyage.getGare_arrive() != null) { %>
                                <%= voyage.getGare_arrive().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="5" class="text-center">Aucun voyage trouvé</td>
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
