<template>
    <v-layout
    style="flex: none;"
    align-center
    column
    >
        <v-avatar
        color="indigo"
        size="36"
        >
            <v-icon>
                mdi-account-circle
            </v-icon>
        </v-avatar>
        <span>{{$props.name}}</span>
        <span>{{$props.email}}</span>
    </v-layout>
</template>

<script>
export default {
    name: "Profile",
    props: [
        "name",
        "email",
    ],
};
</script>

<style scoped>
.row {
    height: 60px;
    margin: 0;
    margin-bottom: 12px;
    flex: 0;
}
.col {
    padding: 0;
}
.v-avatar {
    margin-top: 20px;
    margin-bottom: 20px;
}
.layout {
    margin-bottom: 20px;
    font-size: 10pt;
}
</style>