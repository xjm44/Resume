document.write(
    `
                <div class = "sidebar">
                    <div class = "logo">
                        <a href="/" ><h1>Emerge</h1></a>
                    </div>
                    <ul class="nav">
                        <li>
                            <div class = "Dashboard_nav">
                            <a href="/admin">
                                <i class='bx bxs-dashboard'></i>
                                <span class="links">Dashboard</span>
                            </a>
                            </div>
                        </li>
                        <li>
                            <div class = "Coach_nav">
                            <a href="/admin/Coach">
                                <i class='bx bxs-group'></i>
                                <span class="links">Coach</span>
                            </a>
                            </div>
                        </li>
                        <li>
                            <div class = "Client_nav">
                            <a href="/admin/Client">
                                <i class='bx bxs-group'></i>
                                <span class="links">Leader</span>
                            </a>
                            </div>
                        </li>
                        <li>
                            <div class = "Feedback_nav">
                            <a href="/admin/Feedback">
                                <i class='bx bxs-comment-detail'></i>
                                <span class="links">Feedback</span>
                            </a>
                            </div>
                        </li>
                        <li>
                            <div class = "logout_nav">
                            <a href="/logout">
                                <i class='bx bxs-log-out'></i>
                                <span class="links">Logout</span>
                            </a>
                            </div>
                        </li>
                </div>
                `
)
