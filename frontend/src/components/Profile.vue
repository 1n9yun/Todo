<template>
    <v-layout
    style="flex: none;"
    align-center
    column
    class="pb-5"
    >
        <v-avatar
        class="my-5"
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
.layout {
    font-size: 10pt;
}
</style>