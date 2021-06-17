<template>
    <v-layout>
        <Nav
        :name="password"
        :email="email"
        class="nav"
        />
        <div
        class="main"
        >
            요기는 nav 누른거에 따라서 바뀔꺼에요
        </div>
    </v-layout>
</template>

<script>
import Nav from '@/components/Nav';

export default {
    name: "Main",
    components: {
        Nav,
    },
    created() {
        const email = this.$store.state.user.email;

        if(!email) {
            this.$router.push('login');
        }else {
            this.email = email;
            this.password = this.$store.state.user.password;
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

<style scoped>
.nav {
    min-width: 200px;
    max-width: 200px;
}

.main {
    width: 100%;
}
</style>