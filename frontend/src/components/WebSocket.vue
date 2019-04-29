/* eslint-disable */
<template>
	<div>
		<div id="main-content" class="container">
			<div class="row">
				<div class="col-md-6">
					<form v-if="!connected"
						  ref="loginForm"
						  class="form-inline">
						<div class="form-group">
							<input type="text" id="name" class="form-control" v-model="username" placeholder="Name">
							<input type="password" id="password" class="form-control" v-model="password" placeholder="Password">
							<button id="connect" class="btn btn-primary" :disabled="username.length === 0" type="submit"
									@click.prevent="connect">Connect
							</button>
						</div>
					</form>
					<button v-if="connected"
							id="disconnect" class="btn btn-danger" type="submit" @click.prevent="disconnect">Disconnect
					</button>
				</div>
			</div>
			<div class="row" v-if="connected">
				<div class="col-md-9">
					<table id="conversation" class="table table-striped">
						<thead>
						<tr>
							<th colspan="3">Greetings</th>
						</tr>
						</thead>
						<tbody>
						<tr v-for="item in receivedMessages">
							<td v-if="item.time">{{item.time}} :</td>
							<td v-if="item.name">{{ item.name }} :</td>
							<td v-bind:colspan="item.name ? '1' : '3'">{{ item.message }}</td>
						</tr>
						</tbody>
					</table>
				</div>
				<div class="col-md-3">
					<ul>
						<li v-for="user in joinedUsers">
							{{user.name}} [{{user.state}}]
						</li>
					</ul>
				</div>
			</div>
		</div>
		<footer class="footer" v-if="connected">
			<div class="container">
				<form>
					<div class="row">
						<div class="col-6 m-0 pr-0">
							<input type="text" class="form-control m-0 pr-0" placeholder="Message..." v-model="globalMessage">
						</div>
						<div class="col-3 m-0 pr-0">
							<input type="text" class="form-control m-0 pr-0" placeholder="private receiver..." v-model="receiver">
						</div>
						<div class="col-3 m-0 pl-0">
							<button class="btn btn-primary m-0 pl-0" type="submit" @click.prevent="sendMessage">Send Message</button>
						</div>
					</div>
				</form>
			</div>
		</footer>
	</div>
</template>

<script>
	import SockJS from "sockjs-client";
	import Stomp from "webstomp-client";
	import Vue from "vue";
	import axios from "axios";
	import VueAxios from "vue-axios";

	export default {
		name: "websocketdemo",
		data() {
			return {
				receivedMessages: [],
				joinedUsers: [],
				username: "",
				password: "",
				globalMessage: "",
				socket: {},
				stompClient: {},
				receiver: ""
			};
		},
		computed: {
			connected() {
				return this.stompClient && this.stompClient.connected;
			}
		},
		methods: {
			connect() {
				const loginParams = new URLSearchParams();
				loginParams.append('username', this.username);
				loginParams.append('password', this.password);

				axios.post('/login', loginParams).then((response) => {

					this.socket = new SockJS("/gs-guide-websocket");
					this.stompClient = Stomp.over(this.socket);
					this.stompClient.connect(
						{},
						frame => {
							console.log(frame);

							this.stompClient.subscribe("/topic/globalMessages", tick => {
								console.log("globalMessage", tick);
								this.receivedMessages.push(JSON.parse(tick.body));
							});

							this.stompClient.subscribe("/topic/members/list", tick => {
								console.log("members/list", tick);
								this.joinedUsers = JSON.parse(tick.body);
							});

							this.stompClient.subscribe("/user/queue", tick => {
								console.log("private user message: ", tick);
								this.receivedMessages.push(JSON.parse(tick.body));
							});
						},
						error => {
							console.log(error);
						}
					);
				});
			},
			sendMessage() {
				if (this.receiver) {
					this.sendPrivateMessage();
				} else {
					this.sendGlobalMessage();
				}

			},
			sendPrivateMessage() {
				console.log("Send message to user:" + this.globalMessage);
				if (this.connected) {
					const msg = {
						message: this.globalMessage
					};
					this.stompClient.send("/app/queue/user/" + this.receiver, JSON.stringify(msg), {});
				}
			},
			sendGlobalMessage() {
				console.log("Send message:" + this.globalMessage);
				if (this.connected) {
					const msg = {
						message: this.globalMessage
					};
					this.stompClient.send("/app/sendMessage", JSON.stringify(msg), {});
				}
			},
			disconnect() {
				if (this.stompClient) {
					axios.post('/logout').then((response) => {
						this.stompClient.disconnect();
					});
				}

			}
		},
		mounted() {
			// this.connect();
		}
	};
</script>

<style scoped>
	.footer {
		position: absolute;
		bottom: 0;
		width: 100%;
		height: 60px;
		background-color: #f5f5f5;
	}
</style>