import React, { Component } from "react";
import { Link } from "react-router-dom";
import { BreadCrumbs } from "./templates/BreadCrumbs";
import { connect } from "react-redux";
import { userAccount } from '../actions/AccountActions';
import DataService from '../service/DataService';


class Account extends Component {

    breadCrumbsLink = new Map([["Account", ""]]);

    constructor(props) {
        super(props);
    }

    async componentDidMount() {
        const response = await DataService.userProfileRequest();
        const account = await response.json();
        this.props.userAccount(account);
    }

    render() {
        let account = this.props.account || '';
        return (
            <div>
                <BreadCrumbs links={ this.breadCrumbsLink }/>
                <div className="w-25 account-edit-container">
                    <h1 className="m-4">Account</h1>
                    <form>
                        <div className="form-group">
                            <label><b>Fist name</b></label>
                            <input type="text" className="form-control form-control-lg " id="fistName"
                                   name="firstName" placeholder="Your fist name"
                                   defaultValue={ account.firstName || '' } required autoFocus/>
                        </div>
                        <div className="form-group">
                            <label><b>Last name</b></label>
                            <input type="text" className="form-control form-control-lg" id="lastName"
                                   name="lastName" placeholder="Your last name"
                                   defaultValue={ account.lastName || '' } required/>
                        </div>
                        <div className="form-group">
                            <label><b>Patronymic</b></label>
                            <input type="text" className="form-control form-control-lg" id="patronymic"
                                   name="patronymic" placeholder="Your patronymic"
                                   defaultValue={ account.patronymic || '' }/>
                        </div>
                        <div className="form-group">
                            <label><b>Email</b></label>
                            <input type="email" className="form-control form-control-lg" id="email"
                                   name="email" placeholder="Your email" required
                                   defaultValue={ account.email  || '' }/>
                        </div>
                        <div className="form-group">
                            <label><b>Address</b></label>
                            <input type="text" className="form-control form-control-lg" id="address"
                                   name="address" placeholder="Your address" required
                                   defaultValue={ account.address || '' }/>
                        </div>
                        <div className="form-group">
                            <label><b>Phone</b></label>
                            <input type="text" className="form-control form-control-lg" id="phone"
                                   name="phone" placeholder="Your phone" required
                                   defaultValue={ account.phone || '' }/>
                        </div>
                        <div className="form-group">
                            <label><b>Old password</b></label>
                            <input type="password" className="form-control form-control-lg" id="oldPassword"
                                   name="oldPassword" placeholder="Old password" required/>
                        </div>
                        <div className="form-group">
                            <label><b>New password</b></label>
                            <input type="password" className="form-control form-control-lg" id="newPassword"
                                   name="newPassword" placeholder="New password" required minLength="8"/>
                        </div>
                        <div className="form-group mt-4">
                            <button type="submit" className="btn btn-lg btn-primary btn-block">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        account: state.userState.user
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        userAccount: (account) => dispatch(userAccount(account))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Account);