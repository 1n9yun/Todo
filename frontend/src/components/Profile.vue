<template>
    <v-row
    align="center"
    justify="center"
    >
        <v-col
        cols="3"
        >
            <v-avatar
            color="indigo"
            size="36"
            >
                <v-icon>
                    mdi-account-circle
                </v-icon>
            </v-avatar>
        </v-col>
        <v-col
        clas="15"
        >
            <span>{{$props.name}}</span><br>
            <span>{{$props.email}}</span><br>
        </v-col>
    </v-row>
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
</style>