<template>
    <v-layout
    column
    class="py-3 px-8"
    >
        <h1>{{this.$props.title}}</h1><br>
        <!-- today's todo children -->
        <Todo
        :title="'Test'"
        />
    </v-layout>
</template>

<script>
import Todo from '@/components/Todo';

export default {
    name: "TodoList",
    components: {
        Todo,
    },
    props: [
        "title"
    ]
};
</script>

<style scoped>
</style>