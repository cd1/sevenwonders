package com.gmail.luizjmfilho.sevenwonders.data

import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(tableName = "Person", fromColumnName = "nickname", toColumnName = "name")
class AutoMigration3To4 : AutoMigrationSpec