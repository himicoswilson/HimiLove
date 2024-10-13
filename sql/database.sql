SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `himilove`.`Comment`  (
  `CommentID` int NOT NULL AUTO_INCREMENT,
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `UserID` int NOT NULL,
  `PostID` int NOT NULL,
  `ParentCommentID` int NULL DEFAULT NULL,
  `IsDeleted` tinyint(1) NOT NULL DEFAULT 0,
  `CreatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CommentID`) USING BTREE,
  INDEX `UserID`(`UserID` ASC) USING BTREE,
  INDEX `PostID`(`PostID` ASC) USING BTREE,
  INDEX `ParentCommentID`(`ParentCommentID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Couple`  (
  `CoupleID` int NOT NULL AUTO_INCREMENT,
  `UserID1` int NOT NULL,
  `UserID2` int NOT NULL,
  `AnniversaryDate` date NOT NULL,
  `BgImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`CoupleID`) USING BTREE,
  UNIQUE INDEX `UserID1`(`UserID1` ASC) USING BTREE,
  UNIQUE INDEX `UserID2`(`UserID2` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Entity`  (
  `EntityID` int NOT NULL AUTO_INCREMENT,
  `EntityName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `EntityType` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `CoupleID` int NOT NULL,
  PRIMARY KEY (`EntityID`) USING BTREE,
  INDEX `CoupleID`(`CoupleID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Image`  (
  `ImageID` int NOT NULL AUTO_INCREMENT,
  `ImageURL` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `PostID` int NOT NULL,
  `OrderIndex` int NOT NULL,
  PRIMARY KEY (`ImageID`) USING BTREE,
  INDEX `PostID`(`PostID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Location`  (
  `LocationID` int NOT NULL AUTO_INCREMENT,
  `Latitude` decimal(9, 6) NOT NULL,
  `Longitude` decimal(9, 6) NOT NULL,
  `LocationName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`LocationID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Mood`  (
  `MoodID` int NOT NULL AUTO_INCREMENT,
  `UserID` int NOT NULL,
  `MoodName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `MoodIcon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `StartTime` datetime NOT NULL,
  `EndTime` datetime NULL DEFAULT NULL,
  `IsDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`MoodID`) USING BTREE,
  INDEX `UserID`(`UserID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Post`  (
  `PostID` int NOT NULL AUTO_INCREMENT,
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `UserID` int NOT NULL,
  `CoupleID` int NOT NULL,
  `LocationID` int NULL DEFAULT NULL,
  `CreatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IsDeleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`PostID`) USING BTREE,
  INDEX `UserID`(`UserID` ASC) USING BTREE,
  INDEX `CoupleID`(`CoupleID` ASC) USING BTREE,
  INDEX `LocationID`(`LocationID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Post_Entity`  (
  `PostID` int NOT NULL,
  `EntityID` int NOT NULL,
  PRIMARY KEY (`PostID`, `EntityID`) USING BTREE,
  INDEX `EntityID`(`EntityID` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Post_Tag`  (
  `PostID` int NOT NULL,
  `TagID` int NOT NULL,
  PRIMARY KEY (`PostID`, `TagID`) USING BTREE,
  INDEX `TagID`(`TagID` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`Tag`  (
  `TagID` int NOT NULL AUTO_INCREMENT,
  `TagName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CreatorID` int NOT NULL,
  PRIMARY KEY (`TagID`) USING BTREE,
  INDEX `CreatorID`(`CreatorID` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`User`  (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `NickName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `Avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `RegistrationDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LogoutDate` datetime NULL DEFAULT NULL,
  `UpdatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `IsDeleted` tinyint(1) NOT NULL DEFAULT 0,
  `LastLoginTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastActiveTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`) USING BTREE,
  UNIQUE INDEX `UserName`(`UserName` ASC) USING BTREE,
  UNIQUE INDEX `Email`(`Email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `himilove`.`User_Entity_Last_Viewed`  (
  `UserID` int NOT NULL,
  `EntityID` int NOT NULL,
  `Last_Viewed_Time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`UserID`, `EntityID`) USING BTREE,
  INDEX `EntityID`(`EntityID` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `himilove`.`Comment` ADD CONSTRAINT `Comment_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Comment` ADD CONSTRAINT `Comment_ibfk_2` FOREIGN KEY (`PostID`) REFERENCES `himilove`.`Post` (`PostID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Comment` ADD CONSTRAINT `Comment_ibfk_3` FOREIGN KEY (`ParentCommentID`) REFERENCES `himilove`.`Comment` (`CommentID`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Couple` ADD CONSTRAINT `Couple_ibfk_1` FOREIGN KEY (`UserID1`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Couple` ADD CONSTRAINT `Couple_ibfk_2` FOREIGN KEY (`UserID2`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Entity` ADD CONSTRAINT `Entity_ibfk_1` FOREIGN KEY (`CoupleID`) REFERENCES `himilove`.`Couple` (`CoupleID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Image` ADD CONSTRAINT `Image_ibfk_1` FOREIGN KEY (`PostID`) REFERENCES `himilove`.`Post` (`PostID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Mood` ADD CONSTRAINT `Mood_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post` ADD CONSTRAINT `Post_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post` ADD CONSTRAINT `Post_ibfk_2` FOREIGN KEY (`CoupleID`) REFERENCES `himilove`.`Couple` (`CoupleID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post` ADD CONSTRAINT `Post_ibfk_3` FOREIGN KEY (`LocationID`) REFERENCES `himilove`.`Location` (`LocationID`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post_Entity` ADD CONSTRAINT `Post_Entity_ibfk_1` FOREIGN KEY (`PostID`) REFERENCES `himilove`.`Post` (`PostID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post_Entity` ADD CONSTRAINT `Post_Entity_ibfk_2` FOREIGN KEY (`EntityID`) REFERENCES `himilove`.`Entity` (`EntityID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post_Tag` ADD CONSTRAINT `Post_Tag_ibfk_1` FOREIGN KEY (`PostID`) REFERENCES `himilove`.`Post` (`PostID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Post_Tag` ADD CONSTRAINT `Post_Tag_ibfk_2` FOREIGN KEY (`TagID`) REFERENCES `himilove`.`Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`Tag` ADD CONSTRAINT `Tag_ibfk_1` FOREIGN KEY (`CreatorID`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`User_Entity_Last_Viewed` ADD CONSTRAINT `User_Entity_Last_Viewed_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `himilove`.`User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `himilove`.`User_Entity_Last_Viewed` ADD CONSTRAINT `User_Entity_Last_Viewed_ibfk_2` FOREIGN KEY (`EntityID`) REFERENCES `himilove`.`Entity` (`EntityID`) ON DELETE CASCADE ON UPDATE CASCADE;

SET FOREIGN_KEY_CHECKS=1;