<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thống kê doanh thu</title>
</head>
<body>
<h2>Thống kê doanh thu</h2>

<form action="thong-ke" method="get">
    <select name="loai" onchange="this.form.submit()">
        <option value="ngay" ${loai == 'ngay' ? 'selected' : ''}>Theo ngày</option>
        <option value="thang" ${loai == 'thang' ? 'selected' : ''}>Theo tháng</option>
        <option value="nam" ${loai == 'nam' ? 'selected' : ''}>Theo năm</option>
    </select>
</form>

<table border="1">
    <tr>
        <th>${loai == 'ngay' ? 'Ngày' : (loai == 'thang' ? 'Tháng/Năm' : 'Năm')}</th>
        <th>Doanh thu</th>
    </tr>
    <c:forEach var="tk" items="${thongKe}">
        <tr>
            <td>${tk.ngay != null ? tk.ngay : (tk.thang != null ? tk.thang + "/" + tk.nam : tk.nam)}</td>
            <td>${tk.doanhThu}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
