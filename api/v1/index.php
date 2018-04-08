<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

//including the required files
require_once '../include/DbOperation.php';
require '../vendor/autoload.php';

$app = new \Slim\App();

$container = $app->getContainer();

$container->get('settings')['displayErrorDetails'] = true;


$app->get('/users', function (Request $request, Response $response) {
    $db = new DbOperation();
    $result = $db->getAllUsers();
    
        $data = [];

        $data['users'] = [];

        while($row = $result->fetch_assoc()){
            $user = [];
            $user['id'] = $row['id'];
            $user['email'] = $row['email'];
            $user['name'] = $row['name'];
            $user['mobile_no'] = $row['mobile_no'];
            $user['photo_url'] = $row['photo_url'];
            $user['date_of_registration'] = $row['date_of_registration'];
            $user['links'] = [];
            $selfLink = [];
            $selfLink['href'] = $request->getUri() . '/' .$user['id'];
            $selfLink['rel'] = 'self';
            array_push($user['links'], $selfLink);
            array_push($data['users'],$user);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }
    return $response;
});

$app->get('/users/{uid}', function (Request $request, Response $response) {
    $db = new DbOperation();
    $uid = $request->getAttribute('uid');
    
    $result = $db->getUserByFirebaseUserId($uid);
    
    if($result){
        if($result->num_rows > 0){
            $data = [];
            $data = [];
            $row = $result->fetch_assoc();
            $user = [];
            $user['id'] = $row['id'];
            $user['name'] = $row['name'];
            $user['email'] = $row['email'];
            $user['mobile_no'] = $row['mobile_no'];
            $user['photo_url'] = $row['photo_url'];
            $user['date_of_registration'] = $row['date_of_registration'];
            $user['links'] = [];
            $selfLink = [];
            $selfLink['href'] = (string)$request->getUri();
            $selfLink['rel'] = 'self';
            array_push($user['links'], $selfLink);
            array_push($data,$user);
            $response = writeResponse($request, $response, $data, 200);
        }
        else{
        	$result1 = $db->getUserByUserId($uid);
		    
		    if($result1){
		        if($result1->num_rows > 0){
		            $data = [];
		            $data = [];
		            $row = $result1->fetch_assoc();
		            $user = [];
		            $user['id'] = $row['id'];
		            $user['name'] = $row['name'];
		            $user['email'] = $row['email'];
		            $user['mobile_no'] = $row['mobile_no'];
		            $user['photo_url'] = $row['photo_url'];
		            $user['date_of_registration'] = $row['date_of_registration'];
		            $user['links'] = [];
		            $selfLink = [];
		            $selfLink['href'] = (string)$request->getUri();
		            $selfLink['rel'] = 'self';
		            array_push($user['links'], $selfLink);
		            array_push($data,$user);
		            $response = writeResponse($request, $response, $data, 200);
		        }
		    }
		    else{
	            $data = [];
	            $data["status"] = "data not exists";
	            $response = writeResponse($request, $response, $data, 404);
        	}
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
});

$app->get('/orders/{id}', function (Request $request, Response $response) {
    $db = new DbOperation();
    $id = $request->getAttribute('id');
    
    $result = $db->getOrderById($id);
    
    if($result){
        if($result->num_rows > 0){
            
            $row = $result->fetch_assoc();
            $user = [];
            $user['id'] = $row['id'];
            $user['uid'] = $row['uid'];
            $user['otp'] = $row['otp'];
            $user['amount'] = $row['amount'];
            $user['color'] = $row['color'];
            $user['status'] = $row['status'];
            $user['username'] = $row['username'];
            $result1 = $db->getItemIdByOrderId($id);

            $user['items'] = [];

           	if($result1){

           	}else{
           		$data = [];
           	 	$data["status"] = "data not exists";
            	$response = writeResponse($request, $response, $user, 404);	
           	}
	        while($row = $result1->fetch_assoc()){
	            array_push($user['items'],$row['iid']);
	        }
         
            $response = writeResponse($request, $response, $user, 200);
        }
        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $user, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
});

$app->post('/users', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = [];

    //reading post parameters
    $parsedBody = $request->getParsedBody();
    
    $name = getValueOrDefault('name',$parsedBody, "");
    $email = getValueOrDefault('email',$parsedBody, "");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createUser($name, $email, "", "", "", "");

    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $uid = $request->getAttribute($res['id']);
        $data = [];
        $data['id'] = $res['id'];
  		$response = writeResponse($request, $response, $data, 200);
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while registereing";
        $response = writeResponse($request, $response, $data, 500);
  } else if ($res['error'] == 2) {
        $res = $db->getUserIdByEmailId($email);
        //if($res)
    	if($res->num_rows > 0){
            $row = $res->fetch_assoc();
            $uid = $row['id'];
            $data = [];
            $data['id'] = $uid;
            $response = writeResponse($request, $response, $data, 200);
        }
    }
    return $response;
});


$app->get('/items', function(Request $request, Response $response){
	$db = new DbOperation();

	$params = $request->getQueryParams();
	if($params)
		$ids = $params['ids'];
    if($params && $ids){
    	$ids = urldecode($ids);
    	$items = explode(',',$ids);
    	$data = [];
		foreach($items as $id){
			    $result = $db->getItemById($id);
    
			    if($result){
			        if($result->num_rows > 0){
			            $row = $result->fetch_assoc();
			            $user = [];
			            $user['id'] = $row['id'];
			            $user['price'] = $row['price'];
			            $user['name'] = $row['name'];
			            $user['category'] = $row['category'];
			            $user['description'] = $row['description'];
			            $user['imageUri'] = $row['imageUri'];
			            array_push($data, $user);
			     }
				    else{
					            $data = [];
					            $data["status"] = "data not exists";
					            $response = writeResponse($request, $response, $data, 404);
			        }
			    
			    }
		}
		if($data != null){
			            $response = writeResponse($request, $response, $data, 200);			
		}else{
            $response = writeResponse($request, $response, $data, 204);
        }	
    }else{
    	$result = $db->getAllItems();
		$data = [];

        while($row = $result->fetch_assoc()){
            $item = [];
            $item['id'] = $row['id'];
            $item['price'] = $row['price'];
            $item['name'] = $row['name'];
            $item['category'] = $row['category'];
            $item['description'] = $row['description'];
            $item['imageUri'] = $row['imageUri'];
            array_push($data,$item);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }	
    }
    return $response;
});

$app->get('/sid', function(Request $request, Response $response){
	$db = new DbOperation();
	$result = $db->getAllShops();
	$data = [];
	$data['shops'] = [];

        while($row = $result->fetch_assoc()){
            $item = [];
            $item['sid'] = $row['sid'];
            $item['address'] = $row['address'];
            $item['phone'] = $row['phone'];
            $item['sname'] = $row['sname'];
            $item['scategory'] = $row['scategory'];
            array_push($data['shops'],$item);
        }
        if($data != NULL){
            $response = writeResponse($request, $response, $data, 200);
        }else{
            $response = writeResponse($request, $response, $data, 204);
        }
    return $response;
});

$app->get('/items/{id}', function (Request $request, Response $response) {
    $db = new DbOperation();
    $id = $request->getAttribute('id');
    
        $result = $db->getItemById($id);
    
    if($result){
        if($result->num_rows > 0){
            $row = $result->fetch_assoc();
            $user = [];
            $user['id'] = $row['id'];
            $user['price'] = $row['price'];
            $user['name'] = $row['name'];
            $user['category'] = $row['category'];
            $user['description'] = $row['description'];
            $user['imageUri'] = $row['imageUri'];
            $response = writeResponse($request, $response, $user, 200);
        }
        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $data, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
});



$app->post('/items', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
   
    
    //$id = getValueOrDefault('id',$parsedBody, "");
    $price = getValueOrDefault('price',$parsedBody, "");
    $name = getValueOrDefault('name',$parsedBody, "");
    $category = getValueOrDefault('category',$parsedBody, "");
    $description = getValueOrDefault('description',$parsedBody, "");
    $url = getValueOrDefault('imageUri',$parsedBody, "");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createItem($price, $name, $category, $description, $url);

    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $id = $res['id'];
        $data["error"] = false;
        //Adding a success message
        $data["message"] = "You have successfully inserted an item";
        //Displaying data
        $response = writeResponse($request, $response, $data, 201);
        
    //If the result returned is 1 means failure
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while inserting";
        $response = writeResponse($request, $response, $data, 500);

    //If the result returned is 2 means user already exist
    } else if ($res['error'] == 2) {
        $data["error"] = true;
        $data["message"] = "Sorry, this item already existed";
        $response = writeResponse($request, $response, $data, 403);
    }
    return $response;
});

$app->post('/orders', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
   
    
    //$id = getValueOrDefault('id',$parsedBody, "");
    $uid = getValueOrDefault('uid',$parsedBody, "");
    $otp = getValueOrDefault('otp',$parsedBody, "");
    $amount = getValueOrDefault('amount',$parsedBody, "");
    $color = getValueOrDefault('color', $parsedBody, "");
    $status = getValueOrDefault('status',$parsedBody, "pending");
    $iids = getValueOrDefault('items',$parsedBody, "");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->createOrder($uid, $otp, $amount,$color,$status);
    
    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $id = $res['id'];
        if(is_array($iids) || is_object($iids)){
        	foreach ($iids as $iid) {
        		$res1 = $db->createOrderItems($id,$iid);	
       		}
        }
        
        
        $data["error"] = false;
        //Adding a success message
        $data["message"] = "You have successfully inserted an item";
        //Displaying data
        $response = writeResponse($request, $response, $data, 201);
        
    //If the result returned is 1 means failure
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while inserting";
        $response = writeResponse($request, $response, $data, 500);

    //If the result returned is 2 means user already exist
    } else if ($res['error'] == 2) {
        $data["error"] = true;
        $data["message"] = "Sorry, this item already existed";
        $response = writeResponse($request, $response, $data, 403);
    }
    return $response;
});

$app->patch('/orders', function (Request $request, Response $response) {

    //Verifying the required parameters
//    verifyRequiredParams(array('name', 'username', 'password'));

    //Creating a data array
    $data = array();

    //reading post parameters
    $parsedBody = $request->getParsedBody();
   
    
    $id = getValueOrDefault('id',$parsedBody, "");
    $uid = getValueOrDefault('uid',$parsedBody, "");
    $otp = getValueOrDefault('otp',$parsedBody, "");
    $amount = getValueOrDefault('amount',$parsedBody, "");
    $color = getValueOrDefault('color', $parsedBody, "");
    $status = getValueOrDefault('status',$parsedBody, "pending");
    $iids = getValueOrDefault('ids',$parsedBody, "");
    //Creating a DbOperation object
    $db = new DbOperation();

    //Calling the method createStudent to add student to the database
    $res = $db->updateOrder($id,$uid, $otp, $amount,$color,$status);
    
    //If the result returned is 0 means success
    if ($res['error'] == 0) {
        //Making the data error false
        $id = $res['id'];
        if(is_array($iids) || is_object($iids)){
        	foreach ($iids as $iid) {
        		$res1 = $db->updateOrderItems($id,$iid);	
       		}
        }
        
        
        $data["error"] = false;
        //Adding a success message
        $data["message"] = "You have successfully updated an order";
        //Displaying data
        $response = writeResponse($request, $response, $data, 201);
        
    //If the result returned is 1 means failure
    } else if ($res['error'] == 1) {
        $data["error"] = true;
        $data["message"] = "Oops! An error occurred while upadateing";
        $response = writeResponse($request, $response, $data, 500);

    //If the result returned is 2 means user already exist
    } else if ($res['error'] == 2) {
        $data["error"] = true;
        $data["message"] = "Sorry, this order already existed";
        $response = writeResponse($request, $response, $data, 403);
    }
    return $response;
});

$app->get('/orders', function (Request $request, Response $response) {
    $db = new DbOperation();
   // $id = $request->getAttribute('id');
    
        //$result = $db->getItemById($id);
    $result = $db->getAllOrders();
    if($result){
    	$data=[];
        if($result->num_rows > 0){
            
            /*$row = $result->fetch_assoc();*/
            while($row = $result->fetch_assoc()){
            $user = [];
            $user['id'] = $row['id'];
            $id = $row['id'];
            $user['uid'] = $row['uid'];
            $user['otp'] = $row['otp'];
            $user['amount'] = $row['amount'];
            $user['color'] = $row['color'];
            $user['status'] = $row['status'];
            $user['username'] = $row['username'];
            $result1 = $db->getItemIdByOrderId($id);

            $user['items'] = [];

           	if($result1){

           	}else{
           		$data = [];
           	 	$data["status"] = "data not exists";
            	$response = writeResponse($request, $response, $user, 404);	
           	}
	        while($row = $result1->fetch_assoc()){
	            array_push($user['items'],$row['iid']);
	        }
	        array_push($data,$user);
        }
         
            $response = writeResponse($request, $response, $data, 200);
        }

        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $data, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
        
});

$app->get('/users/{id}/orders', function (Request $request, Response $response) {
    $db = new DbOperation();
    $id = $request->getAttribute('id');
    
    //$result = $db->getItemById($id);
    $result = $db->getAllOrdersById($id);
    if($result){
    	$data=[];
        if($result->num_rows > 0){
            
            /*$row = $result->fetch_assoc();*/
            while($row = $result->fetch_assoc()){
            $user = [];
            $user['id'] = $row['id'];
            $id1 = $row['id'];
            $user['uid'] = $row['uid'];
            $user['otp'] = $row['otp'];
            $user['amount'] = $row['amount'];
            $user['color'] = $row['color'];
            $user['status'] = $row['status'];
            $user['username'] = $row['username'];
            $result1 = $db->getItemIdByOrderId($id1);

            $user['items'] = [];

           	if($result1){

           	}else{
           		$data = [];
           	 	$data["status"] = "data not exists";
            	$response = writeResponse($request, $response, $user, 404);	
           	}
	        while($row = $result1->fetch_assoc()){
	            array_push($user['items'],$row['iid']);
	        }
	        array_push($data,$user);
        }
         
            $response = writeResponse($request, $response, $data, 200);
        }

        else{
            $data = [];
            $data["status"] = "data not exists";
            $response = writeResponse($request, $response, $data, 404);
        }
    }
    else
    {
        $data = [];
        $data["status"] = "Server Error";
        $response = writeResponse($request, $response, $data, 500);
    }
    return $response;
        
});


function writeResponse($request,$response,$data,$status_code){
    $response = $response->withHeader('Content-type', 'application/json');
    $response = $response->withJson($data,$status_code);
    return $response;
}

function getValueOrDefault($key,$array,$default){
    return array_key_exists($key,$array) ? $array[$key] : $default;
}

function authorization($request,$response,$next){
    
}

$app->run();


