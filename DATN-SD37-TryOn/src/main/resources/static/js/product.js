document.addEventListener("DOMContentLoaded", function () {
    fetch("/admin/thongkedoanhthu/data?type=thang") // Fetch dữ liệu theo tháng
        // Thay thế bằng API thực tế của bạn
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                console.error("Không có dữ liệu doanh thu.");
                return;
            }
            renderChart(data);
        })
        .catch(error => console.error("Lỗi khi lấy dữ liệu doanh thu:", error));
});

function renderChart(data) {
    const labels = data.map(item => item.thang);
    const doanhThu = data.map(item => item.tongDoanhThu);

    const ctx = document.getElementById("doanhThuChart").getContext("2d");
    new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Doanh thu theo tháng",
                data: doanhThu,
                backgroundColor: "rgba(54, 162, 235, 0.5)",
                borderColor: "rgba(54, 162, 235, 1)",
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}
