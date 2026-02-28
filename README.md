# Task Tracker CLI

A high-performance, zero-dependency Java command-line application for managing tasks. This project demonstrates professional software engineering principles, including layered architecture, dependency injection, and manual JSON serialization.

## 🚀 Key Features
* **Layered Architecture**: Clear separation between UI (Main), Business Logic (Service), and Persistence (Repository).
* **Manual Persistence**: Custom JSON parser built from scratch using `StringBuilder` for $O(n)$ efficiency—no external libraries like Jackson or Gson.
* **Type Safety**: Robust status management using Java Enums and functional interfaces.
* **Audit Trails**: Automatic tracking of `createdAt` and `updatedAt` timestamps for every task.

## 🛠️ Tech Stack
* **Language**: Java 11+ (OpenJDK 11.0.30 compatible)
* **Data Format**: JSON (Manual Parsing)
* **Architecture**: Service-Repository Pattern
* **Project Source**: [roadmap.sh/projects/task-tracker](https://roadmap.sh/projects/task-tracker)

## 🎮 Usage Guide
### Basic Commands
| Command | Example |
| :--- | :--- |
| **Add** | `java -cp out com.tasktracker.Main add "Finish Java assessment"` |
| **List All** | `java -cp out com.tasktracker.Main list` |
| **List by Status** | `java -cp out com.tasktracker.Main list in-progress` |
| **Update** | `java -cp out com.tasktracker.Main update <id> "Revised Description"` |
| **Delete** | `java -cp out com.tasktracker.Main delete <id>` |

### Status Marking
* `mark-todo <id>`
* `mark-in-progress <id>`
* `mark-done <id>`

## 📥 Installation & Build
1.  **Clone the repository**:
    ```bash
    git clone [git@github.com:Tjanelidze/TaskTracker_CLI.git](https://github.com/Tjanelidze/TaskTracker_CLI.git)
    ```
2.  **Compile the source**:
    ```bash
    javac -d out src/com/tasktracker/**/*.java
    ```
3.  **Run the application**:
    ```bash
    java -cp out com.tasktracker.Main
    ```
