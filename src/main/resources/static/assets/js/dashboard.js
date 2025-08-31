// Sidebar toggle
const sidebarToggle = document.querySelector("#sidebar-toggle");
const sidebar = document.querySelector(".sidebar");

if (sidebarToggle && sidebar) {
    sidebarToggle.addEventListener("click", () => {
        sidebar.classList.toggle("collapsed");
    });
}

// Dummy dashboard stats
const stats = {
    donors: 120,
    recipients: 85,
    requests: 34,
    matches: 27
};

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("donors-count").textContent = stats.donors;
    document.getElementById("recipients-count").textContent = stats.recipients;
    document.getElementById("requests-count").textContent = stats.requests;
    document.getElementById("matches-count").textContent = stats.matches;
});

// Sample dynamic table update
const requestTable = document.querySelector("#requests-table tbody");

if (requestTable) {
    const requests = [
        { id: 1, name: "Rahim", blood: "A+", status: "Pending" },
        { id: 2, name: "Karim", blood: "B-", status: "Matched" },
        { id: 3, name: "Mitu", blood: "O+", status: "Completed" },
    ];

    requests.forEach(req => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${req.id}</td>
            <td>${req.name}</td>
            <td>${req.blood}</td>
            <td>${req.status}</td>
        `;
        requestTable.appendChild(row);
    });
}
