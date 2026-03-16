/*
  SQL Server init script for Project-HSF.

  How to run (SSMS):
  - Open this file and Execute.

  Notes:
  - This schema is derived from current JPA entities in src/main/java/.../entity
  - Entities Chapter/Lesson are currently empty (no table created).
*/

-- 1) Create database (skip if you already have it)
IF DB_ID(N'HSFProject') IS NULL
BEGIN
    CREATE DATABASE HSFProject;
END
GO

USE HSFProject;
GO

-- 2) Tables

-- Role
IF OBJECT_ID(N'dbo.[Role]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[Role] (
        id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        name NVARCHAR(30) NOT NULL UNIQUE,
        description NVARCHAR(255) NULL,
        status BIT NOT NULL CONSTRAINT DF_Role_status DEFAULT (1)
    );
END
GO

-- Users
IF OBJECT_ID(N'dbo.[Users]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[Users] (
        id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        email NVARCHAR(255) NOT NULL,
        password_hash NVARCHAR(255) NOT NULL,
        fullname NVARCHAR(100) NOT NULL,
        phone NVARCHAR(20) NOT NULL,
        role_id INT NOT NULL,
        status NVARCHAR(20) NOT NULL,
        verify_token NVARCHAR(64) NULL,
        verify_token_expires_at DATETIME2 NULL,
        email_verified BIT NOT NULL CONSTRAINT DF_Users_email_verified DEFAULT (0),
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NOT NULL
    );

    CREATE UNIQUE INDEX UX_Users_email ON dbo.[Users](email);
    ALTER TABLE dbo.[Users]
        ADD CONSTRAINT FK_Users_Role
        FOREIGN KEY (role_id) REFERENCES dbo.[Role](id);
END
GO

-- settings (self-reference type_id)
IF OBJECT_ID(N'dbo.[settings]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[settings] (
        id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        name NVARCHAR(20) NOT NULL,
        type_id INT NULL,
        setting_value NVARCHAR(100) NULL,
        priority INT NOT NULL,
        status BIT NOT NULL CONSTRAINT DF_settings_status DEFAULT (1),
        description NVARCHAR(200) NULL
    );

    ALTER TABLE dbo.[settings]
        ADD CONSTRAINT FK_settings_type
        FOREIGN KEY (type_id) REFERENCES dbo.[settings](id);
END
GO

-- category
IF OBJECT_ID(N'dbo.[category]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[category] (
        category_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY
    );
END
GO

-- course
IF OBJECT_ID(N'dbo.[course]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[course] (
        course_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        title NVARCHAR(255) NULL,
        description NVARCHAR(MAX) NULL,
        price FLOAT NOT NULL CONSTRAINT DF_course_price DEFAULT(0),
        level NVARCHAR(255) NULL,
        duration INT NOT NULL CONSTRAINT DF_course_duration DEFAULT(0),
        category_id INT NULL,
        instructor_id INT NULL,
        thumbnail_url NVARCHAR(255) NULL,
        pulished BIT NOT NULL CONSTRAINT DF_course_pulished DEFAULT(0),
        create_at DATETIME2 NULL
    );

    ALTER TABLE dbo.[course]
        ADD CONSTRAINT FK_course_category
        FOREIGN KEY (category_id) REFERENCES dbo.[category](category_id);

    ALTER TABLE dbo.[course]
        ADD CONSTRAINT FK_course_instructor
        FOREIGN KEY (instructor_id) REFERENCES dbo.[Users](id);
END
GO

-- enrollment
IF OBJECT_ID(N'dbo.[enrollment]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[enrollment] (
        enrollment_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        status NVARCHAR(20) NOT NULL,
        registered_at DATETIME2 NULL
    );

    ALTER TABLE dbo.[enrollment]
        ADD CONSTRAINT FK_enrollment_user
        FOREIGN KEY (user_id) REFERENCES dbo.[Users](id);

    ALTER TABLE dbo.[enrollment]
        ADD CONSTRAINT FK_enrollment_course
        FOREIGN KEY (course_id) REFERENCES dbo.[course](course_id);
END
GO

-- Post
IF OBJECT_ID(N'dbo.[Post]', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.[Post] (
        post_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        type NVARCHAR(255) NULL,
        content NVARCHAR(MAX) NULL,
        description NVARCHAR(1000) NULL,
        status NVARCHAR(20) NULL,
        created_at DATETIME2 NULL,
        updated_at DATETIME2 NULL,
        user_id INT NOT NULL
    );

    ALTER TABLE dbo.[Post]
        ADD CONSTRAINT FK_Post_User
        FOREIGN KEY (user_id) REFERENCES dbo.[Users](id);
END
GO

-- 3) Seed minimal data
IF NOT EXISTS (SELECT 1 FROM dbo.[Role] WHERE name = N'ADMIN')
    INSERT INTO dbo.[Role](name, description, status) VALUES (N'ADMIN', N'Administrator', 1);
IF NOT EXISTS (SELECT 1 FROM dbo.[Role] WHERE name = N'MEMBER')
    INSERT INTO dbo.[Role](name, description, status) VALUES (N'MEMBER', N'Default member', 1);
GO

/*
  Optional: seed an admin user (replace password_hash with a BCrypt hash you know the raw password for).
  Login requires: status = 'ACTIVE' and BCrypt matches.
*/
IF NOT EXISTS (SELECT 1 FROM dbo.[Users] WHERE email = N'admin@gmail.com')
BEGIN
    DECLARE @adminRoleId INT = (SELECT TOP 1 id FROM dbo.[Role] WHERE name = N'ADMIN');

    INSERT INTO dbo.[Users](
        email, password_hash, fullname, phone, role_id, status,
        verify_token, verify_token_expires_at, email_verified, created_at, updated_at
    ) VALUES (
        N'admin@gmail.com',
        N'$2a$10$REPLACE_WITH_YOUR_BCRYPT_HASH',
        N'Administrator',
        N'0000000000',
        @adminRoleId,
        N'ACTIVE',
        NULL, NULL, 1,
        SYSUTCDATETIME(), SYSUTCDATETIME()
    );
END
GO

