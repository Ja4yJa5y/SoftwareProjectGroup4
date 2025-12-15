CREATE TABLE USERS (
    USERID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    FIRSTNAME VARCHAR(50) NOT NULL,
    LASTNAME VARCHAR(50) NOT NULL,
    USERNAME VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(200) NOT NULL,      -- stores hashed password
    ROLE VARCHAR(20) NOT NULL            -- Manager | Employee | Customer
);

--------------------------------------------------------
-- BOOK CATALOG
--------------------------------------------------------
CREATE TABLE BOOK (
    BOOKID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TITLE VARCHAR(150) NOT NULL,
    AUTHOR VARCHAR(100) NOT NULL,
    GENRE VARCHAR(20)  NOT NULL,
    TYPE VARCHAR(20)  NOT NULL,          -- book | journal | magazine | ebook
    LANGUAGE VARCHAR(30) NOT NULL,
    PRICE DECIMAL(10,2) NOT NULL,
    AVAILABILITY BOOLEAN NOT NULL
);

--------------------------------------------------------
-- BORROWING TRANSACTIONS
-- Includes tracking of return and due dates
--------------------------------------------------------
CREATE TABLE BORROWEDBOOK (
    BORROWID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    USERID INTEGER NOT NULL,
    BOOKID INTEGER NOT NULL,
    BORROWDATE DATE NOT NULL,
    RETURNDUE_DATE DATE NOT NULL,
    RETURNDATE DATE,                      -- NULL if not returned yet
    STATUS VARCHAR(20) NOT NULL,          -- borrowed | returned

    CONSTRAINT FK_BORROW_USER FOREIGN KEY (USERID) REFERENCES USERS(USERID),
    CONSTRAINT FK_BORROW_BOOK FOREIGN KEY (BOOKID) REFERENCES BOOK(BOOKID)
);

--------------------------------------------------------
-- PURCHASE (Payment Transaction Summary)
--------------------------------------------------------
CREATE TABLE PURCHASE (
    PURCHASEID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    USERID INTEGER NOT NULL,
    PURCHASEDATE DATE NOT NULL,
    PAYMENTMETHOD VARCHAR(20) NOT NULL,    -- Visa | MasterCard | Mada | PayPal
    PAYMENTSTATUS VARCHAR(20) NOT NULL,    -- success | failed
    TOTALCOST DECIMAL(10,2) NOT NULL,

    CONSTRAINT FK_PURCHASE_USER FOREIGN KEY (USERID) REFERENCES USERS(USERID)
);

--------------------------------------------------------
-- PURCHASE_ITEMS (Multiple Books per Purchase)
--------------------------------------------------------
CREATE TABLE PURCHASE_ITEMS (
    ITEMID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PURCHASEID INTEGER NOT NULL,
    BOOKID INTEGER NOT NULL,

    CONSTRAINT FK_ITEM_PURCHASE FOREIGN KEY (PURCHASEID) REFERENCES PURCHASE(PURCHASEID),
    CONSTRAINT FK_ITEM_BOOK FOREIGN KEY (BOOKID) REFERENCES BOOK(BOOKID)
);

--------------------------------------------------------
-- OPTIONAL SAFETY CHECKS (Recommended)
--------------------------------------------------------
ALTER TABLE USERS 
ADD CONSTRAINT CHK_ROLE CHECK (ROLE IN ('Manager','Employee','Customer'));

ALTER TABLE PURCHASE 
ADD CONSTRAINT CHK_PAY_STATUS CHECK (PAYMENTSTATUS IN ('success','failed'));

ALTER TABLE PURCHASE 
ADD CONSTRAINT CHK_PAY_METHOD CHECK (PAYMENTMETHOD IN ('Visa','MasterCard','Mada','PayPal'));