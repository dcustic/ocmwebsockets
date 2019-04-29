module.exports = {
	devServer: {
		host: "172.21.12.10",
		port: 8081,
		proxy: {
			'/':{
				target: 'http://172.21.12.10:8080',
				secure:false,
				changeOrigin:true,
				logLevel: 'debug'
			}
		}
	},
	// Change build paths to make them Maven compatible
	// see https://cli.vuejs.org/config/
	outputDir: 'target/dist',
	assetsDir: 'static'
};