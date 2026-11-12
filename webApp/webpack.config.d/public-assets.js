if (config.devServer && Array.isArray(config.devServer.static)) {
    config.devServer.static.push({
        directory: require("path").resolve(__dirname, "../../../../public"),
        watch: false
    });
}
