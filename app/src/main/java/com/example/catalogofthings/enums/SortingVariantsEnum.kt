package com.example.catalogofthings.enums

enum class SortingVariantsEnum(val variant: String) {
    ON_CREATE_DATE("По дате добавления"),
    ON_UPDATE_DATE("По дате обновления"),
    ON_TITLE("По названию"),
    ON_TYPE("По типу")
}