<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống kê doanh thu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container mt-4">
    <h2 class="mb-4">Thống kê doanh thu</h2>

    <form id="filterForm" class="mb-4 d-flex align-items-center">
        <label for="type" class="me-2 fw-bold">Chọn loại thống kê:</label>
        <select id="type" name="type" class="form-control w-auto">
            <option value="ngay">Theo ngày</option>
            <option value="thang">Theo tháng</option>
            <option value="nam">Theo năm</option>
        </select>
        <button type="submit" class="btn btn-primary ms-3">Lọc</button>
    </form>

    <canvas id="revenueChart"></canvas>

    <table class="table table-bordered mt-4">
        <thead>
        <tr class="table-dark">
            <th>Thời gian</th>
            <th>Doanh thu</th>
        </tr>
        </thead>
        <tbody id="dataTable">
        <c:forEach var="item" items="${thongKeList}">
            <tr>
                <td>${item.thoiGian}</td>
                <td><fmt:formatNumber value="${item.tongDoanhThu}" pattern="#,##0 ₫"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    <%--$(document).ready(function () {--%>
    <%--    function loadChart(type) {--%>
    <%--        $.ajax({--%>
    <%--            url: "/thong-ke",--%>
    <%--            method: "GET",--%>
    <%--            data: {type: type},--%>
    <%--            success: function (response) {--%>
    <%--                const labels = [];--%>
    <%--                const data = [];--%>

    <%--                $("#dataTable").empty();--%>
    <%--                response.forEach(item => {--%>
    <%--                    labels.push(item.thoiGian);--%>
    <%--                    data.push(item.tongDoanhThu);--%>
    <%--                    $("#dataTable").append(--%>
    <%--                        `<tr>--%>
    <%--                            <td>${item.thoiGian}</td>--%>
    <%--                            <td>${new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(item.tongDoanhThu)}</td>--%>
    <%--                        </tr>`--%>
    <%--                    );--%>
    <%--                });--%>

    <%--                if (window.revenueChart) {--%>
    <%--                    window.revenueChart.destroy();--%>
    <%--                }--%>

    <%--                const ctx = document.getElementById('revenueChart').getContext('2d');--%>
    <%--                window.revenueChart = new Chart(ctx, {--%>
    <%--                    type: 'bar',--%>
    <%--                    data: {--%>
    <%--                        labels: labels,--%>
    <%--                        datasets: [{--%>
    <%--                            label: 'Doanh thu',--%>
    <%--                            data: data,--%>
    <%--                            backgroundColor: 'rgba(75, 192, 192, 0.2)',--%>
    <%--                            borderColor: 'rgba(75, 192, 192, 1)',--%>
    <%--                            borderWidth: 1--%>
    <%--                        }]--%>
    <%--                    },--%>
    <%--                    options: {--%>
    <%--                        scales: {--%>
    <%--                            y: { beginAtZero: true }--%>
    <%--                        }--%>
    <%--                    }--%>
    <%--                });--%>
    <%--            }--%>
    <%--        });--%>
    <%--    }--%>

    <%--    $("#filterForm").submit(function (event) {--%>
    <%--        event.preventDefault();--%>
    <%--        const type = $("#type").val();--%>
    <%--        loadChart(type);--%>
    <%--    });--%>

    <%--    loadChart("ngay"); // Mặc định load theo ngày--%>
    <%--});--%>
    document.getElementById("filterForm").addEventListener("submit", function(event) {
        event.preventDefault();
        const type = document.getElementById("type").value;

        fetch(`/thong-ke?type=${type}`)
            .then(response => response.json())
            .then(data => {
                const labels = data.map(item => item.thoiGian);
                const doanhThu = data.map(item => item.tongDoanhThu);

                // Cập nhật biểu đồ
                updateChart(labels, doanhThu);

                // Cập nhật bảng dữ liệu
                updateTable(data);
            });
    });

    function updateChart(labels, data) {
        const ctx = document.getElementById('revenueChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Doanh thu',
                    data: data,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    }

    function updateTable(data) {
        const tbody = document.querySelector("table tbody");
        tbody.innerHTML = "";
        data.forEach(item => {
            tbody.innerHTML += `<tr>
            <td>${item.thoiGian}</td>
            <td>${item.tongDoanhThu.toLocaleString()} ₫</td>
        </tr>`;
        });
    }

</script>

</body>
</html>
