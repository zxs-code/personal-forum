package com.github.code.zxs.core.client;











@FeignClient(value = "basic-authorize")
public interface AuthClient {
    @GetMapping("jwt/publicKey")
    FeignResult<PublicKey> getPublicKey();

    @PostMapping("loginInfo")
    FeignResult<LoginInfoDTO> login(@RequestBody TokenUserDTO tokenUserDTO);

    @DeleteMapping("logout")
    FeignResult<Boolean> logout(@RequestBody List<String> tokens);

    @DeleteMapping("loginInfo")
    FeignResult<Boolean> logout(@RequestBody LoginInfoDTO loginInfo);

    @GetMapping("tokenInfo")
    FeignResult<TokenInfoDTO> getTokenUserDTO(String accessToken);
}
