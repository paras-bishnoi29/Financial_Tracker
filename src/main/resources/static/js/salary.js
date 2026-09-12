let currentSalary = 0;
let financialChart = null;


// ================================
// LOAD SALARY
// ================================

async function loadSalary() {

    try {

        const response = await fetch("/salary");

        if (!response.ok) {
            throw new Error("Failed to load salary");
        }

        const salary = await response.json();

        if (salary) {
            currentSalary = salary.amount;
        } else {
            currentSalary = 0;
        }

        document.getElementById("salaryAmount").textContent =
            "₹" + currentSalary.toLocaleString("en-IN");

    } catch (error) {

        console.error("Error loading salary:", error);

    }
}


// ================================
// LOAD EXPENSES
// ================================

async function loadExpenses() {

    try {

        const response = await fetch("/expense/monthly");

        if (!response.ok) {
            throw new Error("Failed to load monthly expenses");
        }

        const totalExpenses = await response.json();

        createFinancialChart(totalExpenses);

    } catch (error) {

        console.error("Error loading monthly expenses:", error);

    }
}


// ================================
// CREATE CHART
// ================================

function createFinancialChart(totalExpenses) {

    const remaining = Math.max(currentSalary - totalExpenses, 0);

document.getElementById("expenseAmount").textContent =
    "₹" + totalExpenses.toLocaleString("en-IN");

document.getElementById("remainingAmount").textContent =
    "₹" + remaining.toLocaleString("en-IN");

    const canvas = document.getElementById("financialChart");

    if (financialChart) {
        financialChart.destroy();
    }

    financialChart = new Chart(canvas, {

        type: "doughnut",

        data: {

            labels: [
                "Expenses",
                "Remaining"
            ],

            datasets: [{
                data: [
                    totalExpenses,
                    Math.max(currentSalary - totalExpenses, 0)
                ],

                borderWidth: 0
            }]
        },

        options: {

            responsive: true,

            maintainAspectRatio: false,

            cutout: "65%",

            plugins: {

                legend: {
                    position: "bottom",

                    labels: {
                        color: "white",
                        padding: 20
                    }
                },

                tooltip: {

                    callbacks: {

                        label: function(context) {

                            return context.label + ": ₹" +
                                Number(context.raw).toLocaleString("en-IN");
                        }
                    }
                }
            }
        }
    });
}


// ================================
// EDIT SALARY
// ================================

document.getElementById("editSalaryBtn").addEventListener("click", () => {

    const dialog = document.getElementById("salaryDialog");
    const input = document.getElementById("salaryInput");

    input.value = currentSalary;

    dialog.showModal();

    input.focus();
});


// ================================
// CANCEL
// ================================

document.getElementById("cancelSalaryBtn").addEventListener("click", () => {

    document.getElementById("salaryDialog").close();

});


// ================================
// SAVE SALARY
// ================================

document.getElementById("saveSalaryBtn").addEventListener("click", async () => {

    const input = document.getElementById("salaryInput");

    const amount = Number(input.value);

    if (!amount || amount <= 0) {

        alert("Please enter a valid salary.");

        return;
    }

    try {

        const response = await fetch("/salary", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                amount: amount
            })
        });

        if (!response.ok) {
            throw new Error("Failed to save salary");
        }

        currentSalary = amount;

        document.getElementById("salaryAmount").textContent =
            "₹" + currentSalary.toLocaleString("en-IN");

        document.getElementById("salaryDialog").close();

        // Rebuild chart with the new salary
        loadExpenses();

    } catch (error) {

        console.error("Error saving salary:", error);

        alert("Could not save salary.");
    }
});

function loadUser() {

    fetch("/user/about")
        .then(response => response.json())
        .then(data => {

            document.getElementById("userName").textContent = data.username;

        });

}

const dashboardBtn = document.getElementById("dashboardBtn");
dashboardBtn.addEventListener("click", function(event){
    window.location.href = "/dashboard.html";
})

// ================================
// INITIAL LOAD
// ================================

loadUser();
loadSalary().then(() => {
    loadExpenses();
});

const logoutBtn = document.getElementById("logoutBtn");
logoutBtn.addEventListener("click", function (event) {

    event.preventDefault();

    fetch("/user/logout", {
        method: "POST"
    })
    .then(response => response.text())
    .then(data => {

        console.log(data);

        window.location.href = "/index.html";

    });

});
