<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no" name="viewport">
  <title>Quản lý Khách sạn</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin_screen/assets/modules/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin_screen/assets/modules/fontawesome/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin_screen/assets/css/style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin_screen/assets/css/components.css">
</head>

<body>
  <div id="app">
    <div class="main-wrapper main-wrapper-1">
      <div class="navbar-bg"></div>
      
      <nav class="navbar navbar-expand-lg main-navbar">
        <form class="form-inline mr-auto">
          <ul class="navbar-nav mr-3">
            <li><a href="#" data-toggle="sidebar" class="nav-link nav-link-lg"><i class="fas fa-bars"></i></a></li>
          </ul>
        </form>
        <ul class="navbar-nav navbar-right">
          <li class="dropdown"><a href="#" data-toggle="dropdown" class="nav-link dropdown-toggle nav-link-lg nav-link-user">
            <div class="d-sm-none d-lg-inline-block">Xin chào, ${sessionScope.acc.fullName}</div></a>
            <div class="dropdown-menu dropdown-menu-right">
              <a href="logout" class="dropdown-item has-icon text-danger">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
              </a>
            </div>
          </li>
        </ul>
      </nav>