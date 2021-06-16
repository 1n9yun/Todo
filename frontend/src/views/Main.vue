<template>
    <v-layout>
        Hi {{email}} :)
    </v-layout>
</template>

<script>
export default {
    name: "Main",
    components: {
    },
    created() {
        const email = this.$store.state.user.email;

        if(!email) {
            this.$router.push('login');
        }else {
            this.email = email;
            this.$store.state.user.password;
        }
    },
    data() {
        return {
            email: "",
            password: ""
        }
    }
};
</script>
