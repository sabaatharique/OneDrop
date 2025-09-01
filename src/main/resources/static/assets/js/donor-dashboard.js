const donorId = 1; // Replace with logged-in donor ID

const eligibilityCard = document.getElementById("eligibility-card");
const appointmentSection = document.getElementById("appointment-section");
const appointmentCard = document.getElementById("appointment-card");
const cancelBtn = document.getElementById("cancel-btn");
const noAppointment = document.getElementById("no-appointment");
const bookingForm = document.getElementById("booking-form");
const bookBtn = document.getElementById("book-btn");
const confirmBooking = document.getElementById("confirm-booking");
const cancelBooking = document.getElementById("cancel-booking");

const apiBase = "http://localhost:8080/donations"; // Spring Boot backend

// Fetch donor appointment info
async function fetchAppointment() {
    const res = await fetch(`${apiBase}/donor/${donorId}`);
    if (res.status === 204) {
        appointmentSection.classList.add("hidden");
        noAppointment.classList.remove("hidden");
    } else {
        const data = await res.json();
        displayAppointment(data);
    }
}

// Display appointment info
function displayAppointment(data) {
    appointmentSection.classList.remove("hidden");
    noAppointment.classList.add("hidden");

    appointmentCard.innerHTML = `
        <p><strong>Hospital:</strong> ${data.hospital}</p>
        <p><strong>Date:</strong> ${data.appointmentDate}</p>
        <p><strong>Status:</strong> ${data.status}</p>
        <hr>
        <h4>Recipient Information</h4>
        <p><strong>Name:</strong> ${data.recipientName}</p>
        <p><strong>Blood Type:</strong> ${data.recipientBloodType}</p>
        <p><strong>Location:</strong> ${data.recipientLocation}</p>
    `;
}

// Cancel appointment
cancelBtn.addEventListener("click", async () => {
    const res = await fetch(`${apiBase}/cancel/${donorId}`, { method: "DELETE" });
    if (res.ok) {
        alert("Appointment cancelled!");
        fetchAppointment();
    }
});

// Show booking form
bookBtn.addEventListener("click", () => {
    bookingForm.classList.remove("hidden");
});

// Hide booking form
cancelBooking.addEventListener("click", () => {
    bookingForm.classList.add("hidden");
});

// Confirm new booking
confirmBooking.addEventListener("click", async () => {
    const recipientId = document.getElementById("recipientId").value;
    const hospital = document.getElementById("hospital").value;
    const appointmentDate = document.getElementById("appointmentDate").value;

    const payload = { donorId, recipientId, hospital, appointmentDate };

    const res = await fetch(`${apiBase}/book`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    if (res.ok) {
        alert("Appointment booked successfully!");
        bookingForm.classList.add("hidden");
        fetchAppointment();
    } else {
        const error = await res.text();
        alert(error);
    }
});

// Load initial data
fetchAppointment();
