<template>
    <v-layout 
    align-center
    justify-center
    >
        <v-flex
        class="login-form"
        >
            <h1>안녕하세요!</h1><br>
            <v-text-field
            placeholder="이메일"
            outlined
            dense
            v-model="email"
            ></v-text-field>
            <v-text-field
            :type="'password'"
            placeholder="패스워드"
            outlined
            dense
            v-model="password"
            ></v-text-field>
            <v-btn
            style="width: 100%;"
            @click="loginHandler"
            >로그인</v-btn>
        </v-flex>
    </v-layout>
</template>

<script>
import {mapActions} from "vuex";

export default {
    data() {
        return {
            email: "",
            password: "",
        }
    },
    methods: {
        ...mapActions("user", [
            "login"
        ]),
        loginHandler() {
            this.login({
                email: this.email,
                password: this.password,
            }).then(() => this.$router.push('/'));
        }
    }
}
</script>

<style scoped>
.layout {
    height: 100%;
}
.login-form {
    max-width: 300px;
    min-width: 300px;
}
</style>