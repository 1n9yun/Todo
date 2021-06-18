<template>
    <v-btn
    class="justify-start"
    >
        <v-icon
        left
        color="white"
        >{{checked ? 'mdi-checkbox-marked' : 'mdi-checkbox-blank-outline'}}</v-icon>
    {{this.$props.title}}
    </v-btn>
</template>

<script>
export default {
    name: "Todo",
    props: [
        "title",
        "checked"
    ],
};
</script>

<style scoped>
</style>