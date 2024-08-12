package soma.achoom.zigg.global.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import soma.achoom.zigg.global.component.OctetStreamReadMsgConverter


@Configuration
class WebConfig @Autowired constructor(private val octetStreamReadMsgConverter: OctetStreamReadMsgConverter) : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(octetStreamReadMsgConverter)
    }
}