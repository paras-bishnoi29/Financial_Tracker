let editId = null;

const dialog = document.getElementById("expenseDialog");
const addExpenseBtn = document.getElementById("addExpenseBtn");
const submitBtn = document.getElementById("submitBtn");
const cancelBtn = document.getElementById("cancelBtn");
const expenseDate = document.getElementById("expenseDate");
const customDate = document.getElementById("customDate");
const category = document.getElementById("category");
const customCategory = document.getElementById("customCategory");

addExpenseBtn.addEventListener("click", function () {

    editId = null;

    clearForm();

    submitBtn.textContent = "Add Expense";

    dialog.showModal();

});

cancelBtn.addEventListener("click", function () {

    dialog.close();

});

submitBtn.addEventListener("click", function () {

    const name = document.getElementById("name").value;
    const amount = document.getElementById("amount").value;
    const categorySelect = document.getElementById("category");
    const customCategory = document.getElementById("customCategory");

    let category;

    if (categorySelect.value === "Other") {
        category = customCategory.value.trim();
    } else {
        category = categorySelect.value;
    }

    if (
        name.trim() == "" || amount == "" || category.trim() == ""
    ) {
        alert("Please fill all the fields");
        return;
    }

    const expense = {
        name: name,
        amount: amount,
        category: category
    }

    if (editId == null) {
        fetch("/expense", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(expense)
        })
            .then(response => response.text())
            .then(data => {
                dialog.close();

                clearForm();

                const selectedDate = customDate.value;

                if (selectedDate) {
                    loadExpenses(selectedDate);
                    loadCategoryChart(selectedDate);
                }

                loadExpenses();

                loadSummary();

                loadCategoryChart();

            });
    }
    else {
        fetch("/expense/" + editId, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(expense)
        })
            .then(response => response.text())
            .then(data => {
                editId = null;

                submitBtn.textContent = "Add Expense";

                dialog.close();

                clearForm();

                loadExpenses();

                loadSummary();
                loadCategoryChart();


            });
    }

});

category.addEventListener("change", function () {

    if (category.value === "Other") {
        customCategory.style.display = "block";
        customCategory.focus();
    } else {
        customCategory.style.display = "none";
        customCategory.value = "";
    }

});

async function loadExpenses(date = null) {

    // If no date was explicitly provided,
    // use the currently selected filter
    if (date === null) {

        if (expenseDate.value === "custom") {
            date = customDate.value;
        } else {
            date = "";
        }
    }

    let url = "/expense";

    if (date) {
        url += `?date=${date}`;
    }

    try {

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Failed to load expenses");
        }

        const data = await response.json();

        const tableBody = document.getElementById("tableBody");

        tableBody.innerHTML = "";

        data.forEach(function (expense) {

            const row = document.createElement("tr");

            const actionShell = document.createElement("td");

            const nameCell = document.createElement("td");
            nameCell.textContent = expense.name;

            const amountCell = document.createElement("td");
            amountCell.textContent = expense.amount;

            const categoryCell = document.createElement("td");
            categoryCell.textContent = expense.category;

            const deleteButton = document.createElement("button");
            const editButton = document.createElement("button");

            const editImg = document.createElement("img");
            editImg.src = "/images/edit.png";
            editImg.alt = "Edit Icon";

            const editText = document.createTextNode("Edit");

            const deleteImg = document.createElement("img");
            deleteImg.src = "/images/delete.png";
            deleteImg.alt = "Delete Icon";

            const deleteText = document.createTextNode("Delete");

            row.appendChild(nameCell);
            row.appendChild(amountCell);
            row.appendChild(categoryCell);

            editButton.appendChild(editImg);
            editButton.appendChild(editText);

            deleteButton.appendChild(deleteImg);
            deleteButton.appendChild(deleteText);

            actionShell.appendChild(editButton);
            actionShell.appendChild(deleteButton);

            row.appendChild(actionShell);

            tableBody.appendChild(row);


            // DELETE
            deleteButton.addEventListener("click", function () {

                fetch("/expense/" + expense.id, {
                    method: "DELETE"
                })
                    .then(response => response.text())
                    .then(data => {

                        loadSummary();
                        loadExpenses();
                        loadCategoryChart();

                    });

            });


            // EDIT
            editButton.addEventListener("click", function () {

                document.getElementById("name").value = expense.name;
                document.getElementById("amount").value = expense.amount;
                document.getElementById("category").value = expense.category;

                editId = expense.id;

                submitBtn.textContent = "Update Expense";

                dialog.showModal();

            });

        });

    } catch (error) {

        console.error("Error loading expenses:", error);

    }
}



function loadSummary() {
    fetch("/summary")
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.getElementById("totalExpense").textContent =
                "₹" + data.totalExpense.toFixed(2);

            document.getElementById("totalTransactions").textContent =
                data.totalTransactions;

            document.getElementById("highestExpense").textContent =
                "₹" + data.highestExpense.toFixed(2);

        });
}

function loadUser() {

    fetch("/user/about")
        .then(response => response.json())
        .then(data => {

            document.getElementById("userName").textContent = data.username;

        });

}

let categoryChart = null;

function loadCategoryChart(date = "") {

    let url = "/category-summary";

    if (date) {
        url += `?date=${date}`;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {

            const labels = data.map(item => item.category);
            const values = data.map(item => item.total);

            const ctx = document.getElementById("categoryChart");

            if (categoryChart !== null) {
                categoryChart.destroy();
            }

            categoryChart = new Chart(ctx, {
                type: "doughnut",

                data: {
                    labels: labels,

                    datasets: [{
                        data: values
                    }]
                },

                options: {
                    responsive: true,

                    cutout: "65%",

                    plugins: {
                        legend: {
                            position: "bottom"
                        }
                    }
                }
            });
        });
}


expenseDate.addEventListener("change", function () {

    if (expenseDate.value === "custom") {

        customDate.style.display = "block";

        // Get today's date
        const today = new Date();

        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, "0");
        const day = String(today.getDate()).padStart(2, "0");

        const todayString = `${year}-${month}-${day}`;

        // Set today's date automatically
        customDate.value = todayString;

        // Load today's expenses
        loadExpenses(todayString);
        loadCategoryChart(todayString);

    } else {

        customDate.style.display = "none";

        loadExpenses();
        loadCategoryChart();

    }

});

customDate.addEventListener("change", () => {

    const selectedDate = customDate.value;

    if (selectedDate) {
        loadExpenses(selectedDate);
        loadCategoryChart(selectedDate);
    }

});

loadUser();
loadSummary();
loadExpenses();
loadCategoryChart();

function clearForm() {

    document.getElementById("name").value = "";
    document.getElementById("amount").value = "";
    document.getElementById("category").value = "";
    document.getElementById("customCategory").value = "";
    document.getElementById("customCategory").style.display = "none";

}

const salaryBtn = document.getElementById("salaryBtn");
salaryBtn.addEventListener("click", function (event) {
    window.location.href = "/salary.html";
})

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