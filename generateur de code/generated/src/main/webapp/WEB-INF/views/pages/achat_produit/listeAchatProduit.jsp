<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.model.table.AchatProduit" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<AchatProduit> achat_produits = (List<AchatProduit>) request.getAttribute("achat_produits");
    String error = (String) request.getAttribute("error");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liste des achat_produits</title>
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
              <h3 class="page-title">Liste des achat_produits</h3>
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
                    <h4 class="card-title">Liste des achat_produits</h4>
                    <p class="card-description">Tous les achat_produits disponibles</p>
                    <div class="table-responsive">
                      <table class="table table-hover table-striped">
                        <thead>
                          <tr>
                            <th>Id</th>
                            <th>IdProduit</th>
                            <th>Quantite</th>
                            <th>DateAchat</th>
                          </tr>
                        </thead>
                        <tbody>
                          <% if (achat_produits != null && !achat_produits.isEmpty()) {
                               for (AchatProduit achat_produit : achat_produits) { 
%>
                          <tr>
                            <td><%= achat_produit.getId() %></td>
                            <td>
                              <% if (achat_produit.getProduit() != null) { %>
                                <%= achat_produit.getProduit().getNom() %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                            <td><%= achat_produit.getQuantite() %></td>
                            <td>
                              <% if (achat_produit.getDateAchatObject() != null) { %>
                                <%= sdf.format(achat_produit.getDateAchatObject()) %>
                              <% } else { %>
                                Non défini
                              <% } %>
                            </td>
                          </tr>
<% }
                             } else { %>
                          <tr>
                            <td colspan="4" class="text-center">Aucun achat_produit trouvé</td>
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
