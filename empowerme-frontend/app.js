// app.js
console.log("App.js loading...");

const API_BASE_URL = 'http://localhost:8080/api/content';

// --- State ---
let currentView = 'dashboard';
let assets = []; // Now fetching from server

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    console.log("DOM loaded. Initializing...");

    // Menu Toggle
    const el = document.getElementById("wrapper");
    const toggleButton = document.getElementById("menu-toggle");

    if (toggleButton) {
        toggleButton.onclick = function () {
            el.classList.toggle("toggled");
        };
    } else {
        console.error("Menu toggle button not found!");
    }

    // Calculate initial dummy costs for upload
    const assetTypeEl = document.getElementById('assetType');
    if (assetTypeEl) {
        assetTypeEl.addEventListener('change', updateMockCost);
    }

    // Initial Render
    console.log("Calling initial navigateTo('dashboard')...");
    navigateTo('dashboard');
});

// --- Navigation Logic ---
async function navigateTo(view) {
    console.log(`Navigating to: ${view}`);
    currentView = view;

    // Update Active Link in Sidebar
    document.querySelectorAll('.list-group-item').forEach(el => el.classList.remove('active'));

    const activeLink = document.getElementById(`nav-${view}`);
    if (activeLink) activeLink.classList.add('active');

    // Render Content
    const contentDiv = document.getElementById('main-content');
    console.log("contentDiv found:", contentDiv);
    const titleEl = document.getElementById('page-title');

    // Clear content while loading (Text added for visibility in case specific fonts fail)
    contentDiv.innerHTML = '<div class="text-center text-secondary mt-5"><h3>Loading content...</h3><i class="fas fa-spinner fa-spin fa-3x"></i></div>';

    try {
        if (view === 'dashboard') {
            titleEl.textContent = 'Dashboard';
            console.log("Fetching assets for dashboard...");
            await fetchAssets();
            console.log("Assets fetched. Rendering dashboard...");
            contentDiv.innerHTML = renderDashboard();
            console.log("Dashboard rendered.");
        } else if (view === 'module1') {
            titleEl.textContent = 'Content Inventory';
            await fetchAssets();
            contentDiv.innerHTML = renderModule1();
        } else if (view === 'module2') {
            titleEl.textContent = 'Campaign Management';
            await fetchAssets();
            contentDiv.innerHTML = renderModule2();
            populateCampaignDropdown();
        } else if (view === 'module3') {
            titleEl.textContent = 'Clients & Subscription';
            const user = await fetchUser(1);
            contentDiv.innerHTML = renderModule3(user);
        } else if (view === 'module5') {
            titleEl.textContent = 'Analytics Dashboard';
            const html = await renderModule5();
            contentDiv.innerHTML = html;
        } else {
            titleEl.textContent = 'Page Not Found';
            contentDiv.innerHTML = renderPlaceholder('Unknown Module');
        }
    } catch (e) {
        console.error("Navigation error:", e);
        contentDiv.innerHTML = `<div class="alert alert-danger">Failed to load view: ${e.message}</div>`;
    }
}

// --- API Calls ---

async function fetchAssets() {
    console.log("fetchAssets: Starting...");
    try {
        const response = await fetch(API_BASE_URL);
        console.log(`fetchAssets: Response Status ${response.status}`);
        if (!response.ok) {
            throw new Error('Failed to fetch assets');
        }
        assets = await response.json();
        console.log(`fetchAssets: Parsed ${assets.length} assets.`);
    } catch (error) {
        console.error('Error fetching assets:', error);
        // Show a discrete error if it fails (e.g. backend not running)
        assets = [];
        showErrorToast("Could not connect to backend. Is it running?");
    }
}

async function createAsset(assetData) {
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(assetData)
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to create asset');
    }

    return await response.json();
}

async function pushToLive(id) {
    const response = await fetch(`${API_BASE_URL}/${id}/live`, {
        method: 'PUT'
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to update asset');
    }
}

// --- Render Functions ---

function renderDashboard() {
    return `
        <div class="row g-3 my-2">
            <div class="col-md-3">
                <div class="p-3 card-custom d-flex justify-content-around align-items-center">
                    <div>
                        <h3 class="fs-2">${assets.length}</h3>
                        <p class="fs-5 text-secondary">Total Assets</p>
                    </div>
                    <i class="fas fa-images fs-1 primary-text border rounded-full secondary-bg p-3"></i>
                </div>
            </div>

            <div class="col-md-3">
                <div class="p-3 card-custom d-flex justify-content-around align-items-center">
                    <div>
                        <h3 class="fs-2">$${assets.reduce((acc, curr) => acc + (curr.productionCost || 0), 0).toLocaleString()}</h3>
                        <p class="fs-5 text-secondary">Total Value</p>
                    </div>
                    <i class="fas fa-hand-holding-usd fs-1 primary-text border rounded-full secondary-bg p-3"></i>
                </div>
            </div>
            
            <!-- Static Placeholders for other stats -->
            <div class="col-md-3">
                <div class="p-3 card-custom d-flex justify-content-around align-items-center">
                    <div>
                        <h3 class="fs-2">2</h3>
                        <p class="fs-5 text-secondary">Active Campaigns</p>
                    </div>
                    <i class="fas fa-bullhorn fs-1 primary-text border rounded-full secondary-bg p-3"></i>
                </div>
            </div>

            <div class="col-md-3">
                <div class="p-3 card-custom d-flex justify-content-around align-items-center">
                    <div>
                        <h3 class="fs-2">+15%</h3>
                        <p class="fs-5 text-secondary">Engagement</p>
                    </div>
                    <i class="fas fa-chart-line fs-1 primary-text border rounded-full secondary-bg p-3"></i>
                </div>
            </div>
        </div>

        <div class="row my-5">
            <h3 class="fs-4 mb-3">Welcome to EmpowerMe</h3>
            <div class="col">
                <p class="text-secondary">Connected to Spring Boot Backend. Real-time data displayed.</p>
            </div>
        </div>
    `;
}

function renderPlaceholder(moduleName) {
    return `
        <div class="d-flex flex-column align-items-center justify-content-center" style="height: 60vh;">
            <i class="fas fa-tools fa-5x text-secondary mb-4 opacity-25"></i>
            <h3 class="text-light">Component Placeholder</h3>
            <h4 class="primary-text">${moduleName}</h4>
            <div class="alert alert-dark mt-4 border-secondary" role="alert">
                <i class="fas fa-info-circle me-2"></i>
                Assigned to Team Member
            </div>
        </div>
    `;
}

function renderModule1() {
    return `
        <div id="alertPlaceholder"></div>
        <div class="d-flex justify-content-end mb-4">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#uploadModal">
                <i class="fas fa-plus me-2"></i>Upload New Asset
            </button>
        </div>
        
        <div class="row my-4">
            <div class="col">
                <div class="table-responsive card-custom p-3">
                    <table class="table table-glass table-hover">
                        <thead>
                            <tr>
                                <th scope="col">ID</th>
                                <th scope="col">Title</th>
                                <th scope="col">Type</th>
                                <th scope="col">Status</th>
                                <th scope="col">Cost</th>
                                <th scope="col">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="assetsTableBody">
                            ${assets.map(asset => `
                                <tr>
                                    <th scope="row">#${asset.id}</th>
                                    <td>${asset.title}</td>
                                    <td><span class="badge ${asset.type === 'VIDEO' ? 'bg-danger' : 'bg-info'} bg-opacity-75">${asset.type}</span></td>
                                    <td><span class="badge ${getStatusBadge(asset.status)} bg-opacity-75">${asset.status}</span></td>
                                    <td>$${(asset.productionCost || 0).toFixed(2)}</td>
                                    <td>
                                        ${getActions(asset)}
                                    </td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

function getStatusBadge(status) {
    switch (status) {
        case 'LIVE': return 'bg-success';
        case 'DRAFT': return 'bg-warning text-dark';
        case 'FLAGGED': return 'bg-danger';
        default: return 'bg-secondary';
    }
}

function getActions(asset) {
    if (asset.status === 'DRAFT') {
        return `
            <button class="btn btn-sm btn-outline-success" onclick="triggerPushToLive(${asset.id})" title="Push to Live">
                <i class="fas fa-rocket"></i> Push Live
            </button>
        `;
    }
    return `<span class="text-white fs-5"><i class="fas fa-check"></i></span>`;
}

function showError(message) {
    const alertPlaceholder = document.getElementById('alertPlaceholder');
    // If not in Module 1 view, simpler alert
    if (!alertPlaceholder) {
        alert(message);
        return;
    }

    alertPlaceholder.innerHTML = `
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
}

function showErrorToast(message) {
    // A simplified toast/alert accessible from anywhere
    console.error(message);
    // Could implement a global toast container here
}

// --- Action Implementations ---

function updateMockCost() {
    const type = document.getElementById('assetType').value;
    const costDisplay = document.getElementById('costDisplay');
    // Mock logic: Video is more expensive
    const baseCost = type === 'Video' ? 1000 : 100;
    const randomVar = Math.floor(Math.random() * 50);
    const estCost = baseCost + randomVar;
    costDisplay.textContent = `$${estCost.toFixed(2)}`;
    costDisplay.setAttribute('data-cost', estCost);
}

async function submitUpload() {
    const title = document.getElementById('assetTitle').value;
    const type = document.getElementById('assetType').value.toUpperCase(); // Backend expects UPPERCASE enum usually or string
    const fileInput = document.getElementById('assetFile');

    if (!title || !fileInput.files[0]) {
        alert("Please enter a title and select a file");
        return;
    }

    const file = fileInput.files[0];
    const fileSize = file.size;

    // Extract extension
    const fileName = file.name;
    const fileExtension = fileName.substring(fileName.lastIndexOf('.'));

    const newAssetPayload = {
        title: title,
        type: type,
        fileSize: fileSize,
        fileExtension: fileExtension
        // Cost is calculated on backend
        // Status defaults to DRAFT on backend
    };

    try {
        await createAsset(newAssetPayload);

        // Close Modal
        const modalEl = document.getElementById('uploadModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        modal.hide();

        // Reset form
        document.getElementById('uploadForm').reset();

        // Refresh view
        navigateTo('module1');

    } catch (err) {
        showError(err.message);
        // Also close modal to show error on main screen, or keep open?
        // Let's hide modal so they see the red alert on the dashboard as requested
        const modalEl = document.getElementById('uploadModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        modal.hide();
    }
}

async function triggerPushToLive(id) {
    if (confirm("Are you sure you want to push this asset to LIVE?")) {
        try {
            await pushToLive(id);
            navigateTo('module1');
        } catch (err) {
            showError(err.message);
        }
    }
}

// --- User API (Module 3) ---

async function fetchUser(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/users/${id}`);
        if (!response.ok) {
            // Fallback for demo if users aren't seeded yet or backend issue
            return { name: 'Demo User (Offline)', role: 'UNKNOWN', walletBalance: 0.0, riskScore: 0 };
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching user:', error);
        return { name: 'Demo User (Offline)', role: 'ERROR', walletBalance: 0.0, riskScore: 0 };
    }
}

function renderModule3(user) {
    return `
        <div class="row d-flex justify-content-center">
            <div class="col-md-6">
                <div class="card card-custom p-4">
                    <div class="text-center">
                        <i class="fas fa-user-circle fa-6x mb-3 text-secondary"></i>
                        <h2 class="primary-text">${user.name}</h2>
                        <span class="badge bg-info text-dark mb-3">${user.role}</span>
                    </div>
                    <hr class="bg-secondary">
                    <div class="d-flex justify-content-between align-items-center my-3">
                        <span class="fs-5 text-secondary">Wallet Balance</span>
                        <span class="fs-3 text-success fw-bold">$${(user.walletBalance || 0).toFixed(2)}</span>
                    </div>
                     <div class="d-flex justify-content-between align-items-center my-3">
                        <span class="fs-5 text-secondary">Risk Score</span>
                        <span class="fs-4 text-warning fw-bold">${user.riskScore}/100</span>
                    </div>
                    <div class="alert alert-dark mt-3 border-secondary" role="alert">
                        <i class="fas fa-info-circle me-2"></i>
                        Next invoice processing in 15 days.
                    </div>
                </div>
            </div>
        </div>
    `;
}

// --- Campaign Management (Module 2) ---

function renderModule2() {
    return `
        <div id="alertPlaceholder"></div>
        <div class="row">
            <div class="col-md-4">
                <div class="card card-custom p-4">
                    <h4 class="mb-3 text-light">Create New Campaign</h4>
                    <form id="campaignForm">
                        <div class="mb-3">
                            <label class="form-label text-light">Campaign Name</label>
                            <input type="text" class="form-control bg-dark text-light border-secondary" id="campName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-light">Select Content (Live Only)</label>
                            <select class="form-select bg-dark text-light border-secondary" id="campContent" required>
                                <option value="" selected disabled>Select an asset...</option>
                                <!-- Populated via JS -->
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-light">Start Date</label>
                            <input type="date" class="form-control bg-dark text-light border-secondary" id="campStart" required>
                        </div>
                        <div class="mb-3">
                             <label class="form-label text-light">End Date</label>
                            <input type="date" class="form-control bg-dark text-light border-secondary" id="campEnd" required>
                        </div>
                        <div class="alert alert-info border-info bg-transparent text-info small">
                            <i class="fas fa-coins me-1"></i> Cost: $50.00 flat fee
                        </div>
                        <button type="button" class="btn btn-primary w-100" onclick="submitCampaign()">Launch Campaign</button>
                    </form>
                </div>
            </div>

            <div class="col-md-8">
                 <div class="card card-custom p-4">
                    <h4 class="mb-3 text-light">Active Campaigns</h4>
                    <div class="table-responsive">
                        <table class="table table-dark table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Content ID</th>
                                    <th>Start</th>
                                    <th>End</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="campaignTableBody">
                                <!-- Placeholders for now, or fetch if API exists -->
                                <tr><td colspan="6" class="text-center text-muted">No active campaigns yet.</td></tr>
                            </tbody>
                        </table>
                    </div>
                 </div>
            </div>
        </div>
    `;
}

function populateCampaignDropdown() {
    const select = document.getElementById('campContent');
    const liveAssets = assets.filter(a => a.status === 'LIVE');

    if (liveAssets.length === 0) {
        const option = document.createElement('option');
        option.text = "No LIVE assets available";
        option.disabled = true;
        select.add(option);
        return;
    }

    liveAssets.forEach(asset => {
        const option = document.createElement('option');
        option.value = asset.id;
        option.text = `${asset.title} (${asset.type})`;
        select.add(option);
    });

    fetchCampaigns(); // Also load table
}

async function fetchCampaigns() {
    try {
        const response = await fetch('http://localhost:8080/api/campaigns');
        if (response.ok) {
            const campaigns = await response.json();
            const tbody = document.getElementById('campaignTableBody');
            if (campaigns.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-light">No active campaigns found.</td></tr>';
                return;
            }
            tbody.innerHTML = campaigns.map(c => `
                <tr>
                    <td>#${c.id}</td>
                    <td>${c.name}</td>
                    <td>#${c.contentId}</td>
                    <td>${c.startDate}</td>
                    <td>${c.endDate}</td>
                    <td><span class="badge bg-success">ACTIVE</span></td>
                    <td>
                        <button class="btn btn-sm btn-outline-info" onclick="openCommentsModal(${c.id}, '${c.name}')">
                            <i class="fas fa-comments"></i> Comments
                        </button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (e) {
        console.error("Failed to fetch campaigns", e);
    }
}

async function submitCampaign() {
    const name = document.getElementById('campName').value;
    const contentId = document.getElementById('campContent').value;
    const start = document.getElementById('campStart').value;
    const end = document.getElementById('campEnd').value;

    if (!name || !contentId || !start || !end) {
        alert("Please fill all fields");
        return;
    }

    const payload = {
        name: name,
        contentId: parseInt(contentId), // Backend expects Long
        startDate: start,
        endDate: end,
        userId: 1 // Hardcoded for demo
    };

    try {
        const response = await fetch('http://localhost:8080/api/campaigns', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const txt = await response.text();
            throw new Error(txt || "Campaign creation failed");
        }

        // Success
        alert("Campaign Launched! $50 deducted.");
        fetchCampaigns();
        document.getElementById('campaignForm').reset();
    } catch (e) {
        showError(e.message);
    }
}

// --- Interaction & Feedback (Module 4) ---

async function openCommentsModal(campaignId, campaignName) {
    document.getElementById('commentsModalLabel').textContent = `Comments: ${campaignName}`;
    document.getElementById('currentCampaignId').value = campaignId;

    // Clear previous
    document.getElementById('commentsList').innerHTML = '<div class="text-center text-muted">Loading...</div>';

    const modal = new bootstrap.Modal(document.getElementById('commentsModal'));
    modal.show();

    await fetchComments(campaignId);
}

async function fetchComments(campaignId) {
    try {
        const response = await fetch(`http://localhost:8080/api/comments/campaign/${campaignId}`);
        const listEl = document.getElementById('commentsList');

        if (response.ok) {
            const comments = await response.json();
            if (comments.length === 0) {
                listEl.innerHTML = '<div class="text-center text-light">No comments yet. Be the first!</div>';
                return;
            }

            listEl.innerHTML = comments.map(c => `
                <div class="card mb-2 bg-dark border-secondary">
                    <div class="card-body py-2">
                        <div class="d-flex justify-content-between">
                            <small class="text-info fw-bold">${c.author}</small>
                            ${c.isFlagged ? '<span class="badge bg-danger">FLAGGED</span>' : ''}
                        </div>
                        <p class="mb-0 text-light ${c.isFlagged ? 'text-decoration-line-through opacity-50' : ''}">${c.text}</p>
                    </div>
                </div>
            `).join('');
        }
    } catch (e) {
        console.error("Failed to fetch comments", e);
        document.getElementById('commentsList').innerHTML = '<div class="text-danger">Failed to load comments</div>';
    }
}

async function submitComment() {
    const text = document.getElementById('newCommentText').value;
    const campaignId = document.getElementById('currentCampaignId').value;

    if (!text) return;

    const payload = {
        campaignId: parseInt(campaignId),
        text: text,
        author: "Demo User", // Hardcoded for demo
        isFlagged: false
    };

    try {
        const response = await fetch('http://localhost:8080/api/comments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            document.getElementById('newCommentText').value = '';
            await fetchComments(campaignId); // Refresh list
        }
    } catch (e) {
        alert("Failed to post comment");
    }
}

// --- Reporting & Analytics (Module 5) ---

async function renderModule5() {
    const userId = 1; // Demo User
    let stats = { totalAssets: 0, activeCampaigns: 0, totalSpend: 0, engagementScore: 0 };

    try {
        const response = await fetch(`http://localhost:8080/api/reports/${userId}`);
        if (response.ok) stats = await response.json();
    } catch (e) {
        console.error("Failed to fetch report", e);
    }

    return `
        <h2 class="mb-4 text-light">Analytics Dashboard</h2>
        
        <div class="row g-4 mb-4">
            <!-- Total Spend -->
            <div class="col-md-3">
                <div class="card card-custom p-3 bg-dark-card text-light h-100">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-secondary mb-1">Total Spend</h6>
                            <h3 class="mb-0 fw-bold">$${stats.totalSpend}</h3>
                        </div>
                        <i class="fas fa-wallet fa-2x text-success opacity-50"></i>
                    </div>
                </div>
            </div>

            <!-- Active Campaigns -->
             <div class="col-md-3">
                <div class="card card-custom p-3 bg-dark-card text-light h-100">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-secondary mb-1">Active Campaigns</h6>
                            <h3 class="mb-0 fw-bold">${stats.activeCampaigns}</h3>
                        </div>
                        <i class="fas fa-bullhorn fa-2x text-info opacity-50"></i>
                    </div>
                </div>
            </div>

            <!-- Content Assets -->
             <div class="col-md-3">
                <div class="card card-custom p-3 bg-dark-card text-light h-100">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-secondary mb-1">Total Assets</h6>
                            <h3 class="mb-0 fw-bold">${stats.totalAssets}</h3>
                        </div>
                        <i class="fas fa-images fa-2x text-warning opacity-50"></i>
                    </div>
                </div>
            </div>

            <!-- Engagement Score -->
             <div class="col-md-3">
                <div class="card card-custom p-3 bg-dark-card text-light h-100">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="text-secondary mb-1">Engagement Score</h6>
                            <h3 class="mb-0 fw-bold">${stats.engagementScore}</h3>
                        </div>
                        <i class="fas fa-chart-pie fa-2x text-danger opacity-50"></i>
                    </div>
                </div>
            </div>
        </div>

        <div class="card card-custom p-4 bg-dark-card text-light">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="mb-0 text-light">Monthly Performance Report</h4>
                <button class="btn btn-primary" onclick="exportCSV()">
                    <i class="fas fa-download me-2"></i>Export CSV
                </button>
            </div>
            <p class="text-secondary">
                This report aggregates data from all modules (Content, Campaigns, Interactions) to provide a holistic view of your marketing performance.
                Click export to download the raw data for external analysis.
            </p>
        </div>
    `;
}

function exportCSV() {
    const userId = 1;
    window.location.href = `http://localhost:8080/api/reports/${userId}/csv`;
}
