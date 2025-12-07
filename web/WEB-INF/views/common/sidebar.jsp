<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="main-sidebar sidebar-style-2">
    <aside id="sidebar-wrapper">
        <div class="sidebar-brand">
            <a href="dashboard">Smart Hotel</a>
        </div>
        <div class="sidebar-brand sidebar-brand-sm">
            <a href="dashboard">SH</a>
        </div>
        <ul class="sidebar-menu">
            
            <li class="menu-header">Staff Management</li>   
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/staffs">
                    <i class="fas fa-users"></i> <span>Staff</span>
                </a>
            </li>

            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/staffRoles">
                    <i class="fas fa-user-tag"></i> <span>Staff Roles</span>
                </a>
            </li>
            
            <li class="menu-header">Room Management</li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/rooms">
                    <i class="fas fa-users"></i> <span>Room</span>
                </a>
            </li>
            
            <li class="menu-header">Service Management</li>
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/service">
                    <i class="fas fa-users"></i> <span>Service</span>
                </a>
            </li>
            
            <li>
                <a class="nav-link" href="${pageContext.request.contextPath}/service-category">
                    <i class="fas fa-users"></i> <span>Service Category</span>
                </a>
            </li>
        </ul>
    </aside>
</div>